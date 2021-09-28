package com.dbapp.ahcloud.adapter.response;

import com.dbapp.ahcloud.adapter.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huixia.hu
 * Date:     2021/9/28 9:59
 * @version 1.0
 */
@Data
public class ResponseBase<T> implements Serializable {
    private T data;
    private int status_code;
    private String msg;

    public ResponseBase() {
        status_code = ResponseCodeEnum.REQUEST_OPERATION_SUCCESS.code();
        msg = ResponseCodeEnum.REQUEST_OPERATION_SUCCESS.msg();
    }

    public static ResponseBase ErrorResponseBase() {
        return new ResponseBase(ResponseCodeEnum.AUTHENTICATION_FAIL.code(),
                ResponseCodeEnum.AUTHENTICATION_FAIL.msg());
    }

    public ResponseBase(int status_code, String msg) {
        this.status_code = status_code;
        this.msg = msg;
    }

}
