package com.med.common.exception;

import com.med.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private Integer code;
    private String msg;

    public BusinessException(ResultCodeEnum resultCodeEnum){
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMsg();
    }

    public BusinessException(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}