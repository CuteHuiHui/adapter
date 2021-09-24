package com.dbapp.ahcloud.adapter.controller.o01;


import com.alibaba.fastjson.JSONObject;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;
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
    public void addAddressObject(@RequestBody String requestString, @RequestHeader(value = "origin_json",required = false) String originHeader) {
        log.info("addAddressObject requestString is: " + requestString + ", origin header is: " + originHeader);
        AddressObjectReq addressObjectReq = JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(), AddressObjectReq.class);
        addressObjectService.addAddressObject(addressObjectReq);
    }

    @DeleteMapping({"/address/{ip_object_id}"})
    public void deleteAddressObject(@PathVariable("ip_object_id") String ipObjectId,
                                    @RequestHeader(value = "origin_json",required = false) String originHeader) {
        log.info("deleteAddressObject ipObjectId is: " + ipObjectId + ", origin header is: " + originHeader);
        addressObjectService.deleteAddressObject(ipObjectId);
    }

    @PutMapping({"/address/{ip_object_id}"})
    public void modifyAddressObject(@PathVariable("ip_object_id") String ipObjectId,
                                    @RequestHeader(value = "origin_json",required = false) String originHeader, @RequestBody String requestString) {
        log.info("modifyAddressObject object_id is: " + ipObjectId + ", origin header is: " + originHeader);
        AddressObjectReq addressObjectReq = JSONObject.parseObject(JSONObject.parseObject(requestString).getJSONObject("data").toJSONString(), AddressObjectReq.class);
        addressObjectReq.setIpObjectId(ipObjectId);
        addressObjectService.modifyAddressObject(addressObjectReq);
    }
}
