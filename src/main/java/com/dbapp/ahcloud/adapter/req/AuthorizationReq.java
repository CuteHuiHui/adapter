package com.dbapp.ahcloud.adapter.req;

import lombok.Data;

/**
 * @author huixia.hu
 * Date:     2021/9/27 9:57
 * @version 1.0
 */
@Data
public class AuthorizationReq {
    /**
     * HTTP请求地址，例如/V1.0/ctcontrol/O01/address
     */
    private String url;

    /**
     * HTTP方法，例如GET/POST/DELETE/PUT
     */
    private String method;

    /**
     * 查询参数
     */
    private String queryString;

    /**
     * 多个参数以&分割，如果无参数，则为空
     */
    private String other;

    /**
     * 见com.dbapp.ahcloud.adapter.intercepter.ApiInterceptor#authCheck(com.dbapp.ahcloud.adapter.req.AuthorizationReq)
     */
    private String authorization;

    /**
     * 微秒级的Unix时间戳: System.currentTimeMillis() * 1000
     */
    private String tonce;

    /**
     * {"data":{}}
     */
    private String body;

}
