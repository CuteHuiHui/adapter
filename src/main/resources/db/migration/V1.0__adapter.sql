/*
 Navicat Premium Data Transfer

 Source Server         : 0 自己的_localhost
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : ids_adapter

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 30/09/2021 09:26:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_object
-- ----------------------------
DROP TABLE IF EXISTS `address_object`;
CREATE TABLE `address_object`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip_object_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP对象id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对象名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对象描述',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `object_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'type-防护对象地址类型：network, range, host\r\naddress-防护对象的具体值',
  `except_object_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'type-除外防护对象地址类型：network, range, host\r\naddress-除外防护对象的具体值',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0：未删除，1：已删除',
  `gmt_create` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `gmt_modified` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ids_policy
-- ----------------------------
DROP TABLE IF EXISTS `ids_policy`;
CREATE TABLE `ids_policy`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ids_policy_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IDS策略id',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `security_policy_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '引用安全策略ID（列表），每个IPS策略对应至少一个安全策略',
  `ids_policy_template_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IDS策略模板id（1：低级防护模板，2：中级防护模板，3：高级防护模板）',
  `ids_policy_template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IDS策略模板名称',
  `enable` tinyint(1) NOT NULL COMMENT '是否启用，0:不启用，1:启用',
  `logging` tinyint(1) NOT NULL COMMENT '是否开启日志记录，0:不启用，1:启用',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略描述',
  `apt_ip_audit_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '添加成功至APT ip检测集合',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0：未删除，1：已删除',
  `gmt_create` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `gmt_modified` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for security_policy
-- ----------------------------
DROP TABLE IF EXISTS `security_policy`;
CREATE TABLE `security_policy`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `security_policy_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '安全策略id',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略描述',
  `priority` tinyint(1) NULL DEFAULT NULL COMMENT '策略优先级，1为最高',
  `src_zone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源区域',
  `dst_zone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目的区域',
  `protocol` tinyint(1) NULL DEFAULT NULL COMMENT '协议类型，0: ipv4, 1: ipv6',
  `src_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '源地址对象，支持ip_object_id或ip_group_object_id，上限为1000个',
  `dst_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目的地址，支持ip_object_id或ip_group_object_id，上限为1000个',
  `service_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '服务对象，service_object_id或service_group_object_id',
  `time_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '时间对象，time_object_id，默认为[”any”]',
  `app_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '应用对象，app_object_id',
  `url_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'URL对象，url_object_id',
  `action` tinyint(1) NOT NULL COMMENT '动作，0:deny,1:allow',
  `logging` tinyint(1) NOT NULL COMMENT '是否开启日志记录，0:不启用，1:启用',
  `enable` tinyint(1) NOT NULL COMMENT '是否启用，0:不启用，1:启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0：未删除，1：已删除',
  `gmt_create` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `gmt_modified` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
