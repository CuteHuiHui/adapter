package com.dbapp.ahcloud.adapter.service;

import com.dbapp.ahcloud.adapter.model.AddressObject;
import com.dbapp.ahcloud.adapter.req.AddressObjectReq;

import java.util.List;

/**
 * 安全策略
 *
 * @author huixia.hu
 * Date:     2021年09月15日 14:47
 * @version 1.0
 */
public interface AddressObjectService {

    void addAddressObject(AddressObjectReq addressObjectReq);

    void deleteAddressObject(String ipObjectId);

    void modifyAddressObject(AddressObjectReq addressObjectReq);

    List<AddressObject> getAddressObjects(List<String> ipObjectIds);
}