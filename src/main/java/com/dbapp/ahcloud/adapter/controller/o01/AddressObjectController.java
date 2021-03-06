package com.dbapp.ahcloud.adapter.controller.o01;


import com.alibaba.fastjson.JSONObject;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;
import com.dbapp.ahcloud.adapter.response.ResponseBase;
import com.dbapp.ahcloud.adapter.service.impl.AddressObjectServiceImpl;
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

@Slf4j
@RestController
@RequestMapping({"/V1.0/ctcontrol/O01"})
public class AddressObjectController {
    @Autowired
    private AddressObjectServiceImpl addressObjectService;

    @PostMapping({"/address"})
    public ResponseBase addAddressObject(@RequestBody String requestString) {
        log.info("地址对象addAddressObject requestString is: " + requestString);
        AddressObjectReq addressObjectReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        AddressObjectReq.class);
        addressObjectService.addAddressObject(addressObjectReq);
        return new ResponseBase();
    }

    @DeleteMapping({"/address/{ip_object_id}"})
    public ResponseBase deleteAddressObject(@PathVariable("ip_object_id") String ipObjectId
    ) {
        log.info("地址对象deleteAddressObject ipObjectId is: " + ipObjectId);
        addressObjectService.deleteAddressObject(ipObjectId);
        return new ResponseBase();
    }

    @PutMapping({"/address/{ip_object_id}"})
    public ResponseBase modifyAddressObject(@PathVariable("ip_object_id") String ipObjectId
            , @RequestBody String requestString) {
        log.info("地址对象modifyAddressObject object_id is: " + ipObjectId);
        AddressObjectReq addressObjectReq =
                JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(),
                        AddressObjectReq.class);
        addressObjectReq.setIpObjectId(ipObjectId);
        addressObjectService.modifyAddressObject(addressObjectReq);
        return new ResponseBase();
    }
}
