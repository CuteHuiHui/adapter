package com.dbapp.ahcloud.adapter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dbapp.ahcloud.adapter.dao.SecurityPolicyMapper;
import com.dbapp.ahcloud.adapter.model.SecurityPolicy;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;
import com.dbapp.ahcloud.adapter.service.SecurityPolicyService;
import com.dbapp.xplan.common.enums.YesOrNo;
import com.dbapp.xplan.common.exception.ServiceInvokeException;
import com.dbapp.xplan.common.utils.BeanUtil;
import com.dbapp.xplan.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 安全策略
 *
 * @author huixia.hu
 * Date:     2021年09月15日 14:48
 * @version 1.0
 */
@Service
@Slf4j
public class SecurityPolicyServiceImpl implements SecurityPolicyService {
    @Resource
    private SecurityPolicyMapper securityPolicyMapper;

    @Override
    public void addSecurityPolicy(SecurityPolicyReq securityPolicyReq) {
        if (!Objects.isNull(this.selectSecurityPolicy(securityPolicyReq.getSecurityPolicyId()))) {
            throw ServiceInvokeException.newException("securityPolicyId:" + securityPolicyReq.getSecurityPolicyId() + "安全策略已存在");
        }

        SecurityPolicy securityPolicy = this.getSecurityPolicy(securityPolicyReq);
        securityPolicyMapper.insert(securityPolicy);
    }

    @Override
    public void delteSecurityPolicy(String policyId) {
        SecurityPolicy securityPolicy = this.selectSecurityPolicy(policyId);
        if (Objects.isNull(securityPolicy)) {
            throw ServiceInvokeException.newException("securityPolicyId:" + policyId + "安全策略不存在或者已删除");
        }
        securityPolicy.setIsDeleted(YesOrNo.YES.getValue());
        securityPolicyMapper.update(securityPolicy,
                new LambdaQueryWrapper<SecurityPolicy>().eq(SecurityPolicy::getSecurityPolicyId, policyId));
    }

    @Override
    public void modifySecurityPolicy(SecurityPolicyReq securityPolicyReq) {
        if (Objects.isNull(this.selectSecurityPolicy(securityPolicyReq.getSecurityPolicyId()))) {
            throw ServiceInvokeException.newException("securityPolicyId:" + securityPolicyReq.getSecurityPolicyId() + "安全策略不存在或者已删除");
        }
        SecurityPolicy securityPolicy = this.getSecurityPolicy(securityPolicyReq);
        securityPolicyMapper.update(securityPolicy,
                new LambdaQueryWrapper<SecurityPolicy>().eq(SecurityPolicy::getSecurityPolicyId,
                        securityPolicyReq.getSecurityPolicyId()));
    }

    @Override
    public List<SecurityPolicy> getSecurityPolicieList(List<String> securityPolicyIds) {
        List<SecurityPolicy> securityPolicies = securityPolicyMapper.selectList(new LambdaQueryWrapper<SecurityPolicy>()
                .in(SecurityPolicy::getSecurityPolicyId, securityPolicyIds)
                .eq(SecurityPolicy::getIsDeleted, YesOrNo.NO.getValue()));
        return CollectionUtils.isEmpty(securityPolicies) ? new ArrayList<>() : securityPolicies;
    }

    @Override
    public SecurityPolicy getSecurityPolicy(String securityPolicyId) {
        SecurityPolicy securityPolicy =
                securityPolicyMapper.selectOne(Wrappers.<SecurityPolicy>lambdaQuery().eq(SecurityPolicy::getIsDeleted,
                YesOrNo.NO.getValue()).eq(SecurityPolicy::getSecurityPolicyId, securityPolicyId));
        if (Objects.isNull(securityPolicy)) {
            log.error("securityPolicyId:" + securityPolicyId + "安全策略不存在或者已删除");
        }
        return securityPolicy;
    }

    /**
     * 入参转SecurityPolicy实体
     *
     * @param securityPolicyReq
     * @return
     */
    private SecurityPolicy getSecurityPolicy(SecurityPolicyReq securityPolicyReq) {
        SecurityPolicy securityPolicy = BeanUtil.genBean(securityPolicyReq, SecurityPolicy.class);
        securityPolicy.setSrcAddress(JsonUtils.toJSONString(securityPolicyReq.getSrcAddress()));
        securityPolicy.setDstAddress(JsonUtils.toJSONString(securityPolicyReq.getDstAddress()));
        securityPolicy.setServiceItems(JsonUtils.toJSONString(securityPolicyReq.getServiceItems()));
        securityPolicy.setTimeItems(JsonUtils.toJSONString(securityPolicyReq.getTimeItems()));
        securityPolicy.setAppItems(JsonUtils.toJSONString(securityPolicyReq.getAppItems()));
        securityPolicy.setUrlItems(JsonUtils.toJSONString(securityPolicyReq.getUrlItems()));
        return securityPolicy;
    }

    private SecurityPolicy selectSecurityPolicy(String securityPolicyId) {
        return securityPolicyMapper.selectOne(new LambdaQueryWrapper<SecurityPolicy>()
                .eq(SecurityPolicy::getSecurityPolicyId, securityPolicyId)
                .eq(SecurityPolicy::getIsDeleted, YesOrNo.NO.getValue()));
    }
}