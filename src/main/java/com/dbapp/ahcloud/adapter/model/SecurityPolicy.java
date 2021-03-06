package com.dbapp.ahcloud.adapter.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Table: security_policy
 */
@Data
public class SecurityPolicy implements Serializable {
    /**
     * 主键id
     *
     * Table:     security_policy
     * Column:    id
     * Nullable:  false
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 安全策略id
     *
     * Table:     security_policy
     * Column:    security_policy_id
     * Nullable:  false
     */
    private String securityPolicyId;

    /**
     * 租户id
     *
     * Table:     security_policy
     * Column:    tenant_id
     * Nullable:  false
     */
    private String tenantId;

    /**
     * 策略名称
     *
     * Table:     security_policy
     * Column:    name
     * Nullable:  false
     */
    private String name;

    /**
     * 策略描述
     *
     * Table:     security_policy
     * Column:    description
     * Nullable:  true
     */
    private String description;

    /**
     * 策略优先级，1为最高
     *
     * Table:     security_policy
     * Column:    priority
     * Nullable:  true
     */
    private Integer priority;

    /**
     * 源区域
     *
     * Table:     security_policy
     * Column:    src_zone
     * Nullable:  true
     */
    private String srcZone;

    /**
     * 目的区域
     *
     * Table:     security_policy
     * Column:    dst_zone
     * Nullable:  true
     */
    private String dstZone;

    /**
     * 协议类型，0: ipv4, 1: ipv6
     *
     * Table:     security_policy
     * Column:    protocol
     * Nullable:  true
     */
    private Integer protocol;

    /**
     * 源地址对象，支持ip_object_id或ip_group_object_id，上限为1000个
     *
     * Table:     security_policy
     * Column:    src_address
     * Nullable:  false
     */
    private String srcAddress;

    /**
     * 目的地址，支持ip_object_id或ip_group_object_id，上限为1000个
     *
     * Table:     security_policy
     * Column:    dst_address
     * Nullable:  false
     */
    private String dstAddress;

    /**
     * 服务对象，service_object_id或service_group_object_id
     *
     * Table:     security_policy
     * Column:    service_items
     * Nullable:  true
     */
    private String serviceItems;

    /**
     * 时间对象，time_object_id，默认为[”any”]
     *
     * Table:     security_policy
     * Column:    time_items
     * Nullable:  false
     */
    private String timeItems;

    /**
     * 应用对象，app_object_id
     *
     * Table:     security_policy
     * Column:    app_items
     * Nullable:  true
     */
    private String appItems;

    /**
     * URL对象，url_object_id
     *
     * Table:     security_policy
     * Column:    url_items
     * Nullable:  true
     */
    private String urlItems;

    /**
     * 动作，0:deny,1:allow
     *
     * Table:     security_policy
     * Column:    action
     * Nullable:  false
     */
    private Integer action;

    /**
     * 是否开启日志记录，0:不启用，1:启用
     *
     * Table:     security_policy
     * Column:    logging
     * Nullable:  false
     */
    private Integer logging;

    /**
     * 是否启用，0:不启用，1:启用
     *
     * Table:     security_policy
     * Column:    enable
     * Nullable:  false
     */
    private Integer enable;

    /**
     * 是否删除，0：未删除，1：已删除
     *
     * Table:     security_policy
     * Column:    is_deleted
     * Nullable:  false
     */
    private Integer isDeleted;

    /**
     * 创建时间
     *
     * Table:     security_policy
     * Column:    gmt_create
     * Nullable:  false
     */
    private Date gmtCreate;

    /**
     * 修改时间
     *
     * Table:     security_policy
     * Column:    gmt_modified
     * Nullable:  false
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table security_policy
     *
     * @mbggenerated Wed Sep 15 18:23:41 CST 2021
     */
    private static final long serialVersionUID = 1L;
}