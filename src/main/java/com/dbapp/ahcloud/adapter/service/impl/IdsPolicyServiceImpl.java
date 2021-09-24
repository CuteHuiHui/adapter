package com.dbapp.ahcloud.adapter.service.impl;

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
import com.dbapp.ahcloud.adapter.sdk.req.IpAuditDTO;
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
import java.util.List;
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

    @Override
    public void addIdsPolicy(IdsPolicyReq idsPolicyReq) {
        List<String> securityPolicyIds = idsPolicyReq.getSecurityPolicyIds();
        //获取安全策略
        List<SecurityPolicy> securityPolicies = securityPolicyService.getSecurityPolicies(securityPolicyIds);

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
        List<AddressObject> addressObjects = addressObjectService.getAddressObjects(ipObjectIds);

        //获取所有需要检测的IP
        List<String> totalAuditIps = new ArrayList<>();
        addressObjects.stream().map(o -> JSONObject.parseArray(o.getObjectList(),
                AddressObjectReq.ObjectListItem.class))
                .map(o -> o.stream().map(AddressObjectReq.ObjectListItem::getAddress).collect(Collectors.toList()))
                .collect(Collectors.toList()).forEach(o -> totalAuditIps.addAll(o));
        auditClient.getAccessKey();
        auditClient.getToken();
        try {
            for (String ip : totalAuditIps) {
                auditClient.addOrUpdate(new IpAuditDTO(null, ip, idsPolicyReq.getDescription(),idsPolicyReq.getEnable()));
            }
        } catch (Exception e) {
            log.error("APT添加或修改IP检测配置失败,开始删除此次已添加成功的");
            List<Integer> needDelete =
                    auditClient.list().stream().filter(o -> totalAuditIps.contains(o.getIp())).map(IpAuditDTO::getId).collect(Collectors.toList());
            auditClient.delete(needDelete.toArray(new Integer[]{}));
            throw ServiceInvokeException.newException("APT添加或修改IP检测配置失败", e);
        }
        //此次添加成功至APT的IP检测id集合
        List<Integer> ids = auditClient.list().stream().filter(o -> totalAuditIps.contains(o.getIp())).map(IpAuditDTO::getId).collect(Collectors.toList());

        //TODO totalFilterIps
        //获取所有不需要检测的IP
        List<String> totalFilterIps = new ArrayList<>();
        addressObjects.stream().map(o -> JSONObject.parseArray(o.getExceptObjectList(),
                AddressObjectReq.ObjectListItem.class))
                .map(o -> o.stream().map(AddressObjectReq.ObjectListItem::getAddress).collect(Collectors.toList()))
                .collect(Collectors.toList()).forEach(o -> totalFilterIps.addAll(o));

        IdsPolicy idsPolicy = this.getIdsPolicy(idsPolicyReq);
        idsPolicy.setAptIpAuditIds(JsonUtils.toJSONString(ids));
        idsPolicyMapper.insert(idsPolicy);
    }

    @Override
    public void delteIdsPolicy(String idsPolicyId) {
        List<IdsPolicy> idsPolicies = idsPolicyMapper.selectList(new LambdaQueryWrapper<IdsPolicy>()
                .eq(IdsPolicy::getIdsPolicyId, idsPolicyId)
                .eq(IdsPolicy::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(idsPolicies)) {
            String error = "idsPolicyId:" + idsPolicyId + "IDS策略不存在或者已删除";
            throw ServiceInvokeException.newException(error);
        }else {
            IdsPolicy idsPolicy = new IdsPolicy();
            idsPolicy.setIsDeleted(YesOrNo.YES.getValue());
            idsPolicyMapper.update(idsPolicy,
                    new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, idsPolicyId));
        }

    }

    @Override
    public void modifyIdsPolicy(IdsPolicyReq idsPolicyReq) {
        List<IdsPolicy> idsPolicies = idsPolicyMapper.selectList(new LambdaQueryWrapper<IdsPolicy>()
                .eq(IdsPolicy::getIdsPolicyId, idsPolicyReq.getIdsPolicyId())
                .eq(IdsPolicy::getIsDeleted, YesOrNo.NO.getValue()));
        if (CollectionUtils.isEmpty(idsPolicies)) {
            String error = "idsPolicyId:" + idsPolicyReq.getIdsPolicyId() + "IDS策略不存在或者已删除";
            throw ServiceInvokeException.newException(error);
        }else {
            IdsPolicy idsPolicy = this.getIdsPolicy(idsPolicyReq);
            idsPolicyMapper.update(idsPolicy,
                    new LambdaQueryWrapper<IdsPolicy>().eq(IdsPolicy::getIdsPolicyId, idsPolicyReq.getIdsPolicyId()));
        }
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

}