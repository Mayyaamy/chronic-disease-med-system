package com.med.auth.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException {
    private String msg;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }
}