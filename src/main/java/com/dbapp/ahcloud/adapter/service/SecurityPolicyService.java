package com.dbapp.ahcloud.adapter.service;

import com.dbapp.ahcloud.adapter.model.SecurityPolicy;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;

import java.util.List;

/**
 * 安全策略
 *
 * @author huixia.hu
 * Date:     2021年09月15日 14:47
 * @version 1.0
 */
public interface SecurityPolicyService {

    void addSecurityPolicy(SecurityPolicyReq securityPolicyReq);

    void delteSecurityPolicy(String policyId);

    void modifySecurityPolicy(SecurityPolicyReq securityPolicyReq);

    SecurityPolicy getSecurityPolicy(String securityPolicyId);

    List<SecurityPolicy> getSecurityPolicies(List<String> securityPolicyIds);

}