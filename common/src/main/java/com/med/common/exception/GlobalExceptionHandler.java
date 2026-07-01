package com.med.common.exception;

import com.med.common.result.Result;
import com.med.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常拦截器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局兜底异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常：",e);
        return Result.fail(ResultCodeEnum.FAIL.getCode(), ResultCodeEnum.FAIL.getMsg());
    }

    // 自定义业务异常单独处理
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        return Result.fail(e.getCode(), e.getMsg());
    }
}