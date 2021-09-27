package com.dbapp.ahcloud.adapter.enums;

/**
 * 错误响应码
 * @author huixia.hu
 * Date:     2021/9/27 13:30
 * @version 1.0
 */
public enum ResponseCodeEnum {
    REQUEST_OPERATION_SUCCESS(200),
    REQUEST_OPERATION_PROCESSING(202),
    REQUEST_ARGUMENTS_WRONG(400),
    AUTHENTICATION_FAIL(401),
    WITHOUT_PRIORITY(403),
    NO_RESULT(404),
    ALREADY_EXISTS(405),
    INTERNAL_ERROR(500);

    private int status_code;

    ResponseCodeEnum(int status_code) {
        this.status_code = status_code;
    }

    public int code() {
        return this.status_code;
    }

    public String msg() {
        String message;
        switch (this.status_code) {
            case 200:
                message = "请求操作成功";
                break;
            case 202:
                message = "请求操作已执行，正在处理中";
                break;
            case 400:
                message = "错误请求：API参数错误";
                break;
            case 401:
                message = "未授权访问";
                break;
            case 403:
                message = "禁止访问";
                break;
            case 404:
                message = "没有查找到结果!";
                break;
            case 405:
                message = "配置项已经存在!";
                break;
            case 500:
                message = "系统错误";
                break;
            default:
                message = "其他未知错误";
        }

        return message;
    }
}
