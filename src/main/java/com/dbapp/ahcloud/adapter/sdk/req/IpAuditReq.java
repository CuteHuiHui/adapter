package com.dbapp.ahcloud.adapter.sdk.req;

import lombok.Data;

/**
 * @author huixia.hu
 * Date:     2021/9/22 13:40
 * @version 1.0
 */
@Data
public class IpAuditReq {
    private Integer id;
    private String ip;
    private String desc;
    private Integer enable;
}
