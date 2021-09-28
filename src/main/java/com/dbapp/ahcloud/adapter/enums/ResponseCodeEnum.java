package com.dbapp.ahcloud.adapter.enums;

/**
 * 错误响应码
 *
 * @author huixia.hu
 * Date:     2021/9/27 13:30
 * @version 1.0
 */
public enum ResponseCodeEnum {
    REQUEST_OPERATION_SUCCESS("200", "请求操作成功"),
    REQUEST_OPERATION_PROCESSING("202", "请求操作已执行，正在处理中"),
    REQUEST_ARGUMENTS_WRONG("400", "错误请求：API参数错误"),
    AUTHENTICATION_FAIL("401", "未授权访问"),
    WITHOUT_PRIORITY("403", "禁止访问"),
    NO_RESULT("404", "没有查找到结果!"),
    ALREADY_EXISTS("405", "配置项已经存在!"),
    INTERNAL_ERROR("500", "系统错误");

    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(String code) {
        for (ResponseCodeEnum value : values()) {
            if (value.code.equals(code)) {
                return value.msg;
            }
        }
        return "其他未知错误";
    }
}
