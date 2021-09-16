package com.dbapp.ahcloud.adapter.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lousaibiao
 * @since 2021/5/26
 */
@Data
@Builder
public class IpsPolicyReq implements Serializable {
    @JsonProperty("ips_policy_id")
    private String ipsPolicyId;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("security_policy_ids")
    private List<String> securityPolicyIds;
    @JsonProperty("ips_policy_template_id")
    private String templateId;
    @JsonProperty("ips_policy_template_name")
    private String templateName;
    @JsonProperty("enable")
    private int enable;
    @JsonProperty("logging")
    private int logging;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
}
