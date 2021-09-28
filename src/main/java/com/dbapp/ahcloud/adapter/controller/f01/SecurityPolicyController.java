package com.dbapp.ahcloud.adapter.controller.f01;

import com.alibaba.fastjson.JSONObject;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;
import com.dbapp.ahcloud.adapter.response.ResponseBase;
import com.dbapp.ahcloud.adapter.service.impl.SecurityPolicyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huixia.hu
 * Date:     2021/9/13 15:50
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping({"/V1.0/ctcontrol/F01"})
public class SecurityPolicyController {


    @Autowired
    private SecurityPolicyServiceImpl securityPolicyService;

    @PostMapping({"/policy"})
    public ResponseBase addSecurityPolicy(@RequestBody String requestString) {
        log.info("安全策略addSecurityPolicy requestString is: " + requestString);
        SecurityPolicyReq securityPolicyReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        SecurityPolicyReq.class);
        securityPolicyService.addSecurityPolicy(securityPolicyReq);
        return new ResponseBase();
    }

    @DeleteMapping({"/policy/{policy_id}"})
    public ResponseBase deleteSecurityPolicy(@PathVariable("policy_id") String policyId) {
        log.info("安全策略deleteSecurityPolicy policy_id is: " + policyId);
        securityPolicyService.delteSecurityPolicy(policyId);
        return new ResponseBase();
    }

    @PutMapping({"/policy/{policy_id}"})
    public ResponseBase modifySecurityPolicy(@PathVariable("policy_id") String policyId,
                                             @RequestBody String requestString) {
        log.info("安全策略modifySecurityPolicy policy_id is: " + policyId + ",requestString is: " + requestString);
        SecurityPolicyReq securityPolicyReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        SecurityPolicyReq.class);
        securityPolicyReq.setSecurityPolicyId(policyId);
        securityPolicyService.modifySecurityPolicy(securityPolicyReq);
        return new ResponseBase();
    }
}
