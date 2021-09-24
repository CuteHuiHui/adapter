package com.dbapp.ahcloud.adapter.service;

import com.dbapp.ahcloud.adapter.req.IdsPolicyReq;

/**
 * 安全策略
 * @author   huixia.hu
 * Date:     2021年09月15日 14:47
 * @version  1.0
 */
public interface IdsPolicyService {

    void addIdsPolicy(IdsPolicyReq idsPolicyReq);

    void delteIdsPolicy(String idsPolicyId);

    void modifyIdsPolicy(IdsPolicyReq idsPolicyReq);


}