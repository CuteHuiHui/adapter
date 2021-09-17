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




//    private static final Logger log = LoggerFactory.getLogger(IPv4AddressNorthAPI.class);
//    @Autowired
//    private IPAddressServiceImpl ipAddressService;
//    @Autowired
//    private NormalizationServiceImpl normalizationService;
//
//    public IPv4AddressNorthAPI() {
//    }
//
//    @PostMapping({"/address/{device_info}/{other}"})
//    public String addAddress(@RequestBody String requestString, @PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation) {
//        log.info("Request content is: " + requestString);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//        JSONObject requestJSON = JSONObject.parseObject(requestString);
//
//        try {
//            responseJSON = this.ipAddressService.add(requestJSON.getJSONObject("data"), "ipv4_single", deviceLocation, deviceInfo);
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
//    @DeleteMapping({"/address/{object_id}/{device_info}/{other}"})
//    public String deleteAddress(@PathVariable("object_id") String objectId, @PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation) {
//        log.info("Request content is: " + objectId);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            if (UUIDUtil.checkIsUuid(objectId)) {
//                responseJSON = this.ipAddressService.delete(objectId, "ipv4_single", deviceLocation, deviceInfo);
//            } else {
//                responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//                responseDataJSON.put("msg", "id不符合uuid");
//                responseJSON.put("data", responseDataJSON);
//            }
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
//    @PutMapping({"/address/{object_id}/{device_info}/{other}"})
//    public String modifyAddress(@PathVariable("object_id") String objectId, @PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @RequestBody String requestString) {
//        log.info("Request object_id: " + objectId + ", content is: " + requestString);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//        JSONObject requestJSON = JSONObject.parseObject(requestString);
//
//        try {
//            if (UUIDUtil.checkIsUuid(objectId)) {
//                responseJSON = this.ipAddressService.modify(requestJSON.getJSONObject("data"), objectId, "ipv4_single", deviceLocation, deviceInfo);
//            } else {
//                responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//                responseDataJSON.put("msg", "id不符合uuid");
//                responseJSON.put("data", responseDataJSON);
//            }
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
//    @GetMapping({"/address/{object_id}/{device_info}/{other}"})
//    public String findAddressById(@PathVariable("object_id") String objectId, @PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation) {
//        log.info("Request objectId: " + objectId);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            if (UUIDUtil.checkIsUuid(objectId)) {
//                responseJSON = this.ipAddressService.search(objectId, "ipv4_single", deviceLocation, deviceInfo);
//            } else {
//                responseJSON.put("status_code", ResponseCodeEnums.REQUEST_ARGUMENTS_WRONG.code());
//                responseDataJSON.put("msg", "id不符合uuid");
//                responseJSON.put("data", responseDataJSON);
//            }
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
//    @GetMapping({"/addresses/{device_info}/{other}"})
//    public String findAddressByPage(@PathVariable("device_info") String deviceInfo, @RequestHeader("device_location") String deviceLocation, @RequestParam("batch") int batch, @RequestParam("batch_size") int batchSize) {
//        log.info("Request page: " + batch + ", batchSize: " + batchSize);
//        JSONObject responseJSON = new JSONObject();
//        JSONObject responseDataJSON = new JSONObject();
//
//        try {
//            responseJSON = this.ipAddressService.batchSearch(batch, batchSize, "ipv4_single", deviceLocation, deviceInfo);
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
//
//
//    @GetMapping({"/address/{object_id}"})
//    public String findAddressById(@PathVariable("object_id") String objectId, @RequestHeader("origin_json") String originHeader) {
//        log.info("Request object_id: " + objectId + ", origin header is: " + originHeader);
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
//    @GetMapping({"/addresses"})
//    public String findAddressByPage(@RequestHeader("origin_json") String originHeader, @RequestParam("batch") int batch, @RequestParam("batch_size") int batchSize) {
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
}
