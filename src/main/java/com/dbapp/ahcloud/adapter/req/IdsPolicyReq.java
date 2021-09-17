package com.dbapp.ahcloud.adapter.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class IdsPolicyReq {
    @JsonProperty("ids_policy_id")
    private String idsPolicyId;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("security_policy_ids")
    private List<String> securityPolicyIds;
    @JsonProperty("ids_policy_template_id")
    private String idsPolicyTemplateId;
    @JsonProperty("ids_policy_template_name")
    private String idsPolicyTemplateName;
    @JsonProperty("enable")
    private Integer enable;
    @JsonProperty("logging")
    private Integer logging;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
}
