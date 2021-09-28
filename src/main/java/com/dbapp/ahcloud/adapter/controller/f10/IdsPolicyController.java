package com.dbapp.ahcloud.adapter.controller.f10;

import com.alibaba.fastjson.JSONObject;
import com.dbapp.ahcloud.adapter.req.IdsPolicyReq;
import com.dbapp.ahcloud.adapter.response.ResponseBase;
import com.dbapp.ahcloud.adapter.service.impl.IdsPolicyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    @PostMapping({"/policy"})
    public ResponseBase addIdsPolicy(@RequestBody String requestString) {
        log.info("IDS策略addIdsPolicy requestString is: " + requestString);
        IdsPolicyReq idsPolicyReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        IdsPolicyReq.class);
        idsPolicyService.addIdsPolicy(idsPolicyReq);
        return new ResponseBase();
    }

    @DeleteMapping({"/policy/{ids_policy_id}"})
    public ResponseBase deleteIdsPolicy(@PathVariable("ids_policy_id") String idsPolicyId) {
        log.info("IDS策略deleteIdsPolicy idsPolicyId is: " + idsPolicyId);
        idsPolicyService.delteIdsPolicy(idsPolicyId);
        return new ResponseBase();
    }

    @PutMapping({"/policy/{ids_policy_id}"})
    public ResponseBase modifyIdsPolicy(@PathVariable("ids_policy_id") String idsPolicyId,
                                        @RequestBody String requestString) {
        log.info("IDS策略modifyIdsPolicy idsPolicyId is: " + idsPolicyId + ",requestString is: " + requestString);
        IdsPolicyReq idsPolicyReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        IdsPolicyReq.class);
        idsPolicyReq.setIdsPolicyId(idsPolicyId);
        idsPolicyService.modifyIdsPolicy(idsPolicyReq);
        return new ResponseBase();
    }
}
