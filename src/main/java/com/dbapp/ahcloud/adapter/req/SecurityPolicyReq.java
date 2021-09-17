package com.dbapp.ahcloud.adapter.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class SecurityPolicyReq {
    @JsonProperty("security_policy_id")
    private String securityPolicyId;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("priority")
    private Integer priority;
    @JsonProperty("src_zone")
    private String srcZone;
    @JsonProperty("dst_zone")
    private String dstZone;
    @JsonProperty("protocol")
    private Integer protocol;
    @JsonProperty("src_address")
    private List<String> srcAddress;
    @JsonProperty("dst_address")
    private List<String> dstAddress;
    @JsonProperty("service_items")
    private List<String> serviceItems;
    @JsonProperty("time_items")
    private List<String> timeItems;
    @JsonProperty("app_items")
    private List<String> appItems;
    @JsonProperty("url_items")
    private List<String> urlItems;
    @JsonProperty("action")
    private Integer action;
    @JsonProperty("logging")
    private Integer logging;
    @JsonProperty("enable")
    private Integer enable;

}
