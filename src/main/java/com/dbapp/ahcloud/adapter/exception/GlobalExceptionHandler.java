package com.dbapp.ahcloud.adapter.exception;

import com.dbapp.ahcloud.adapter.response.ResponseBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author huixia.hu
 * Date:     2021/9/28 10:52
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseBase exception(Exception ex) {
        log.error("", ex);
        if (ex instanceof DefineException) {
            return ResponseBase.otherErrorResponseBase(ex.getMessage());
        } else {
            return ResponseBase.internalErrorResponseBase();
        }
    }
}
