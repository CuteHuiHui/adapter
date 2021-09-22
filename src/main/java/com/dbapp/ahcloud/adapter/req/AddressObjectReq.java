package com.dbapp.ahcloud.adapter.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 地址对象
 * @author   huixia.hu
 * Date:     2021年09月17日 9:58
 * @version  1.0
 */
@Data
public class AddressObjectReq{
    @JsonProperty("ip_object_id")
    private String ipObjectId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("object_list")
    private List<AddressObjectReq.ObjectListItem> objectList;
    @JsonProperty("except_object_list")
    private List<AddressObjectReq.ObjectListItem> exceptObjectList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectListItem {
        @JsonProperty("type")
        private String type;
        @JsonProperty("address")
        private String address;
    }

}
