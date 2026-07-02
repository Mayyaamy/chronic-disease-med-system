package com.med.common.exception;

import com.med.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}