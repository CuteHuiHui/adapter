package com.dbapp.ahcloud.adapter.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dbapp.ahcloud.adapter.dao.SecurityPolicyMapper;
import com.dbapp.ahcloud.adapter.model.SecurityPolicy;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;
import com.dbapp.ahcloud.adapter.service.SecurityPolicyService;
import com.dbapp.xplan.common.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 安全策略
 * @author   huixia.hu
 * Date:     2021年09月15日 14:48
 * @version  1.0
 */
@Service
@Slf4j
public class SecurityPolicyServiceImpl implements SecurityPolicyService {
    @Resource
    private SecurityPolicyMapper securityPolicyMapper;

    @Override
    public void addSecurityPolicy(SecurityPolicyReq securityPolicyReq) {
        SecurityPolicy securityPolicy = BeanUtil.genBean(securityPolicyReq, SecurityPolicy.class);
        securityPolicy.setSrcAddress(StringUtils.join(securityPolicyReq.getSrcAddress(),";"));
        securityPolicy.setDstAddress(StringUtils.join(securityPolicyReq.getDstAddress(),";"));
        securityPolicy.setServiceItems(StringUtils.join(securityPolicyReq.getServiceItems(),";"));
        securityPolicy.setTimeItems(StringUtils.join(securityPolicyReq.getTimeItems(),";"));
        securityPolicy.setAppItems(StringUtils.join(securityPolicyReq.getAppItems(),";"));
        securityPolicy.setUrlItems(StringUtils.join(securityPolicyReq.getUrlItems(),";"));
        securityPolicyMapper.insert(securityPolicy);
    }
}