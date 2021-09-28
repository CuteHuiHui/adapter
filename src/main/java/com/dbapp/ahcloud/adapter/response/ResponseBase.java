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
        status_code = Integer.valueOf(ResponseCodeEnum.REQUEST_OPERATION_SUCCESS.getCode());
        msg = ResponseCodeEnum.REQUEST_OPERATION_SUCCESS.getMsg();
    }

    public static ResponseBase internalErrorResponseBase() {
        return new ResponseBase(Integer.valueOf(ResponseCodeEnum.INTERNAL_ERROR.getCode()),
                ResponseCodeEnum.INTERNAL_ERROR.getMsg());
    }

    public static ResponseBase otherErrorResponseBase(String status_code) {
        return new ResponseBase(Integer.valueOf(status_code), ResponseCodeEnum.getMsg(status_code));
    }

    public ResponseBase(int status_code, String msg) {
        this.status_code = status_code;
        this.msg = msg;
    }

}
