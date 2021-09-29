package com.dbapp.ahcloud.adapter.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dbapp.ahcloud.adapter.dao.IdsPolicyMapper;
import com.dbapp.ahcloud.adapter.model.AddressObject;
import com.dbapp.ahcloud.adapter.model.IdsPolicy;
import com.dbapp.ahcloud.adapter.model.SecurityPolicy;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;
import com.dbapp.ahcloud.adapter.req.IdsPolicyReq;
import com.dbapp.ahcloud.adapter.sdk.AptIpAuditClient;
import com.dbapp.ahcloud.adapter.sdk.dto.IpAuditDTO;
import com.dbapp.ahcloud.adapter.service.IdsPolicyService;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.exception.ServiceInvokeException;
import com.dbapp.xplan.common.utils.BeanUtil;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 安全策略
 *
 * @author huixia.hu
 * Date:     2021年09月15日 14:48
 * @version 1.0
 */
@Service
@Slf4j
public class IdsPolicyServiceImpl implements IdsPolicyService {
    @Resource
    private IdsPolicyMapper idsPolicyMapper;
    @Autowired
    private AptIpAuditClient auditClient;
    @Autowired
    private SecurityPolicyServiceImpl securityPolicyService;
    @Autowired
    private AddressObjectServiceImpl addressObjectService;

    public static final String ALL_IP_AUDIT = "allIpAudit";
    public static final String ALL_IP_FILTER = "allIpFilter";

    @Override
    public void addIdsPolicy(IdsPolicyReq idsPolicyReq) {
        if (!Objects.isNull(this.selectIdsPolicy(idsPolicyReq.getIdsPolicyId()))) {
            throw ServiceInvokeException.newException("idsPolicyId:" + idsPolicyReq.getIdsPolicyId() + "ids策略已存在");
        }

        auditClient.getAccessKey();
        auditClient.getToken();
        //APT添加或修改IP检测
        List<Integer> ids = this.addIpAudit(idsPolicyReq);

        IdsPolicy idsPolicy = this.getIdsPolicy(idsPolicyReq);
        idsPolicy.setAptIpAuditIds(JsonUtils.toJSONString(ids));
        idsPolicyMapper.insert(idsPolicy);
    }



    @Override
    public void delteIdsPolicy(String idsPolicyId) {
        IdsPolicy idsPolicy = this.selectIdsPolicy(idsPolicyId);
        if (Objects.isNull(idsPolicy)) {
            throw ServiceInvokeException.newException("idsPolicyId:" + idsPolicyId + "IDS策略不存在或者已删除");
        }
        auditClient.getAccessKey();
        auditClient.getToken();
        auditClient.delete(JsonUtils.parseArray(idsPolicy.getAptIpAuditIds(), Integer.class).toArray(new Integer[]{}));

        idsPolicy.setIsDeleted(YesOrNo.YES.getValue());
        idsPolicyMapper.update(idsPolicy,
                new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, idsPolicyId));
    }

    @Override
    public void modifyIdsPolicy(IdsPolicyReq idsPolicyReq) {
        IdsPolicy idsPolicy = this.selectIdsPolicy(idsPolicyReq.getIdsPolicyId());
        if (Objects.isNull(idsPolicy)) {
            throw ServiceInvokeException.newException("idsPolicyId:" + idsPolicyReq.getIdsPolicyId() + "IDS策略不存在或者已删除");
        }

        List<Integer> ids = JsonUtils.parseArray(idsPolicy.getAptIpAuditIds(), Integer.class);
        String description = idsPolicyReq.getDescription();
        Integer enable = idsPolicyReq.getEnable();
        List<String> newSecurityPolicyIds = idsPolicyReq.getSecurityPolicyIds();
        List<String> oldSecurityPolicyIds = JsonUtils.parseArray(idsPolicy.getSecurityPolicyIds(), String.class);

        auditClient.getAccessKey();
        auditClient.getToken();
        //策略集合不变：状态变更、描述变更
        if (newSecurityPolicyIds.containsAll(oldSecurityPolicyIds)) {
            if (!enable.equals(idsPolicy.getEnable()) || !description.equals(idsPolicy.getDescription())) {
                for (Integer id : ids) {
                    IpAuditDTO ipAuditDTO = auditClient.get(id);
                    ipAuditDTO.setEnable(enable);
                    ipAuditDTO.setDesc(description);
                    auditClient.addOrUpdate(ipAuditDTO);
                }
            }
        } else {
            List<String> list1 = oldSecurityPolicyIds.stream().collect(Collectors.toList());
            List<String> list2 = newSecurityPolicyIds.stream().collect(Collectors.toList());
            //oldSecurityPolicyIds与newSecurityPolicyIds的差集，则删除
            list1.removeAll(newSecurityPolicyIds);
            List<String> allIpAudits = this.getAuditOrFilterIp(list1).get(ALL_IP_AUDIT);

            List<IpAuditDTO> existList = auditClient.list();
            if (CollectionUtils.isNotEmpty(existList)) {
                //此次需要删除APT的IP检测id集合
                List<Integer> needDeleteIds =
                        existList.stream().filter(o -> allIpAudits.contains(o.getIp())).map(IpAuditDTO::getId).collect(Collectors.toList());
                ids.removeAll(needDeleteIds);
                auditClient.delete(needDeleteIds.toArray(new Integer[]{}));
            }

            //newSecurityPolicyIds与oldSecurityPolicyIds的差集，则添加
            list2.removeAll(oldSecurityPolicyIds);
            IdsPolicyReq req = new IdsPolicyReq();
            req.setSecurityPolicyIds(list2);
            req.setEnable(enable);
            req.setDescription(description);
            ids.addAll(this.addIpAudit(req));
        }

        IdsPolicy idsPolicy1 = this.getIdsPolicy(idsPolicyReq);
        idsPolicy1.setAptIpAuditIds(JsonUtils.toJSONString(ids));
        idsPolicyMapper.update(idsPolicy1,
                new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, idsPolicyReq.getIdsPolicyId()));
    }




    /**
     * APT添加IP检测
     * @param idsPolicyReq
     * @return
     */
    private List<Integer> addIpAudit(IdsPolicyReq idsPolicyReq) {
        List<String> allIpAudits = this.getAuditOrFilterIp(idsPolicyReq.getSecurityPolicyIds()).get(ALL_IP_AUDIT);

        List<IpAuditDTO> existList = auditClient.list();
        if (CollectionUtils.isNotEmpty(existList)) {
            List<String> repeatList = existList.stream().filter(o -> allIpAudits.contains(o.getIp())).map(IpAuditDTO::getIp).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(repeatList)) {
                throw ServiceInvokeException.newException("以下IP已存在:" + JSONUtil.toJsonStr(repeatList));
            }
        }

        for (String ip : allIpAudits) {
            auditClient.addOrUpdate(new IpAuditDTO(null, ip, idsPolicyReq.getDescription(), idsPolicyReq.getEnable()));
        }

        //此次添加成功至APT的IP检测id集合
        return auditClient.list().stream().filter(o -> allIpAudits.contains(o.getIp())).map(IpAuditDTO::getId).collect(Collectors.toList());
    }

    /**
     * 获取审计或者过滤IP
     * @param securityPolicyIds
     * @return
     */
    private Map<String,List<String>> getAuditOrFilterIp(List<String> securityPolicyIds) {
        //获取安全策略
        List<SecurityPolicy> securityPolicies = securityPolicyService.getSecurityPolicieList(securityPolicyIds);

        //获取地址对象
        List<String> ipObjectIds = new ArrayList<>();
        securityPolicies.stream()
                .map(SecurityPolicy::getSrcAddress)
                .map(o -> JSONObject.parseArray(o, String.class)).collect(Collectors.toList())
                .forEach(o -> ipObjectIds.addAll(o));
        securityPolicies.stream()
                .map(SecurityPolicy::getDstAddress)
                .map(o -> JSONObject.parseArray(o, String.class)).collect(Collectors.toList())
                .forEach(o -> ipObjectIds.addAll(o));
        List<AddressObject> addressObjects = addressObjectService.getAddressObjectList(ipObjectIds);

        //获取所有需要检测的IP
        List<String> allIpAudits = new ArrayList<>();
        addressObjects.stream().map(o -> JSONObject.parseArray(o.getObjectList(),
                AddressObjectReq.ObjectListItem.class))
                .map(o -> o.stream().map(AddressObjectReq.ObjectListItem::getAddress).collect(Collectors.toList()))
                .collect(Collectors.toList()).forEach(o -> allIpAudits.addAll(o));

        //获取所有不需要检测的IP
        List<String> allIpFilters = new ArrayList<>();
        addressObjects.stream().map(o -> JSONObject.parseArray(o.getExceptObjectList(),
                AddressObjectReq.ObjectListItem.class))
                .map(o -> o.stream().map(AddressObjectReq.ObjectListItem::getAddress).collect(Collectors.toList()))
                .collect(Collectors.toList()).forEach(o -> allIpFilters.addAll(o));

        Map<String, List<String>> map = new HashMap<>();
        map.put(ALL_IP_AUDIT, allIpAudits);
        map.put(ALL_IP_FILTER, allIpFilters);
        return map;
    }

    /**
     * 入参转IdsPolicy实体
     * @param idsPolicyReq
     * @return
     */
    private IdsPolicy getIdsPolicy(IdsPolicyReq idsPolicyReq) {
        IdsPolicy idsPolicy = BeanUtil.genBean(idsPolicyReq, IdsPolicy.class);
        idsPolicy.setSecurityPolicyIds(JsonUtils.toJSONString(idsPolicyReq.getSecurityPolicyIds()));
        return idsPolicy;
    }

    private IdsPolicy selectIdsPolicy(String idsPolicyId) {
        return idsPolicyMapper.selectOne(new LambdaQueryWrapper<IdsPolicy>()
                .eq(IdsPolicy::getIdsPolicyId, idsPolicyId)
                .eq(IdsPolicy::getIsDeleted, YesOrNo.NO.getValue()));
    }
}