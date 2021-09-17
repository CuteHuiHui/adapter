package com.dbapp.ahcloud.adapter.controller.f10;

import com.alibaba.fastjson.JSONObject;
import com.dbapp.ahcloud.adapter.req.IdsPolicyReq;
import com.dbapp.ahcloud.adapter.req.SecurityPolicyReq;
import com.dbapp.ahcloud.adapter.service.impl.IdsPolicyServiceImpl;
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
@RequestMapping({"/V1.0/ctcontrol/F10"})
public class IdsPolicyController {


    @Autowired
    private IdsPolicyServiceImpl idsPolicyService;

    //TODO 返回实体、required = false、错误处理、请求日志

    @PostMapping({"/policy"})
    public void addIdsPolicy(@RequestBody String requestString, @RequestHeader(value = "origin_json",required = false) String originHeader) {
        log.info("addIdsPolicy requestString is: " + requestString + ", origin header is: " + originHeader);
        IdsPolicyReq idsPolicyReq = JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(), IdsPolicyReq.class);
        idsPolicyService.addIdsPolicy(idsPolicyReq);
    }

    @DeleteMapping({"/policy/{ids_policy_id}"})
    public void deleteIdsPolicy(@PathVariable("ids_policy_id") String ids_policy_id,
                                       @RequestHeader(value = "origin_json",required = false) String originHeader) {
        log.info("deleteIdsPolicy ids_policy_id is: " + ids_policy_id + ", origin header is: " + originHeader);
        idsPolicyService.delteIdsPolicy(ids_policy_id);
    }

    @PutMapping({"/policy/{ids_policy_id}"})
    public void modifyIdsPolicy(@PathVariable("ids_policy_id") String ids_policy_id,
                                       @RequestHeader(value = "origin_json",required = false) String originHeader,
                                       @RequestBody String requestString) {
        log.info("modifyIdsPolicy ids_policy_id is: " + ids_policy_id + ",requestString is: " +requestString + ",origin header is: " + originHeader);
        IdsPolicyReq idsPolicyReq = JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(), IdsPolicyReq.class);
        idsPolicyReq.setIdsPolicyId(ids_policy_id);
        idsPolicyService.modifyIdsPolicy(idsPolicyReq);
    }



//    @DeleteMapping({"/policy/{policy_id}/{device_info}/{other}"})
//    public String deleteSecurityPolicy(@PathVariable("device_info") String deviceInfo, @RequestHeader(
//            "device_location") String deviceLocation, @PathVariable("policy_id") String policyId) {
//        log.info("Request content is: " + policyId);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            responseJSON = this.securityPolicyService.delete(policyId, deviceLocation, deviceInfo);
//        } catch (Exception var7) {
//            var7.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//



//    @PostMapping({"/policy/{device_info}/{other}"})
//    public String addSecurityPolicy(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @RequestBody String requestString) {
//        log.info("Request content is: " + requestString);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//        JSONObject requestJSON = JSONObject.parseObject(requestString);
//
//        try {
//            responseJSON = this.securityPolicyService.add(requestJSON.getJSONObject("data"), deviceLocation, deviceInfo);
//        } catch (Exception var8) {
//            var8.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//
//    @DeleteMapping({"/policy/{policy_id}/{device_info}/{other}"})
//    public String deleteSecurityPolicy(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @PathVariable("policy_id") String policyId) {
//        log.info("Request content is: " + policyId);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            responseJSON = this.securityPolicyService.delete(policyId, deviceLocation, deviceInfo);
//        } catch (Exception var7) {
//            var7.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//
//    @PutMapping({"/policy/{policy_id}/{device_info}/{other}"})
//    public String modifySecurityPolicy(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @PathVariable("policy_id") String policyId, @RequestBody String requestString) {
//        log.info("Request policy: " + policyId + ", content is: " + requestString);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//        JSONObject requestJSON = JSONObject.parseObject(requestString);
//
//        try {
//            responseJSON = this.securityPolicyService.modify(requestJSON.getJSONObject("data"), policyId, deviceLocation, deviceInfo);
//        } catch (Exception var9) {
//            var9.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//
//    @GetMapping({"/policy/{policy_id}/{device_info}/{other}"})
//    public String findSecurityPolicyById(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @PathVariable("policy_id") String policyId) {
//        log.info("Request policyId: " + policyId);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            responseJSON = this.securityPolicyService.search(policyId, deviceLocation, deviceInfo);
//        } catch (Exception var7) {
//            var7.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//
//    @GetMapping({"/policys/{device_info}/{other}"})
//    public String findSecurityPolicyByPage(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @RequestParam("batch") int batch, @RequestParam("batch_size") int batchSize) {
//        log.info("Request page: " + batch + ", batchSize: " + batchSize);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            responseJSON = this.securityPolicyService.batchSearch(batch, batchSize, deviceLocation, deviceInfo);
//        } catch (Exception var8) {
//            var8.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }
//
//    @PostMapping({"/policy/move/{device_info}/{other}"})
//    public String moveSecurityPolicy(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @RequestBody String requestString) {
//        log.info("Request content is: " + requestString);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//        JSONObject requestJSON = JSONObject.parseObject(requestString);
//
//        try {
//            responseJSON = this.securityPolicyService.move(requestJSON.getJSONObject("data"), deviceLocation, deviceInfo);
//        } catch (Exception var8) {
//            var8.printStackTrace();
//            responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//            responseDataJSON.put("msg", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.msg());
//            responseJSON.put("data", responseDataJSON);
//        }
//
//        return responseJSON.toJSONString();
//    }


//
//
//    @GetMapping({"/policy/{policy_id}"})
//    public String findSecurityPolicyById(@PathVariable("policy_id") String policyId, @RequestHeader("origin_json") String originHeader) {
//        log.info("Request policy_id: " + policyId + ", origin header is: " + originHeader);
//        String responseJSON = null;
//
//        try {
//            responseJSON = this.normalizationService.normalizationInterface("GET", originHeader, (String)null);
//        } catch (Exception var5) {
//            var5.printStackTrace();
//        }
//
//        return responseJSON;
//    }
//
//    @GetMapping({"/policys"})
//    public String findSecurityPolicyByPage(@RequestHeader("origin_json") String originHeader, @RequestParam("batch") int batch, @RequestParam("batch_size") int batchSize) {
//        log.info("origin header is: " + originHeader);
//        String responseJSON = null;
//
//        try {
//            responseJSON = this.normalizationService.normalizationInterface("GET", originHeader, (String)null);
//        } catch (Exception var6) {
//            var6.printStackTrace();
//        }
//
//        return responseJSON;
//    }
//
//    @PostMapping({"/policy/move"})
//    public String moveSecurityPolicy(@RequestHeader("origin_json") String originHeader, @RequestBody String requestString) {
//        log.info("Request content is: " + requestString + ", origin header is: " + originHeader);
//        String responseJSON = null;
//
//        try {
//            responseJSON = this.normalizationService.normalizationInterface("POST", originHeader, requestString);
//        } catch (Exception var5) {
//            var5.printStackTrace();
//        }
//
//        return responseJSON;
//    }
}