package com.dbapp.ahcloud.adapter.service;

import com.dbapp.ahcloud.adapter.req.AddressObjectReq;

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

}