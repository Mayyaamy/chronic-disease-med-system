package com.med.common.exception;

import com.med.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 兜底全局未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> globalExceptionHandler(Exception e) {
        log.error("系统未知异常：", e);
        return Result.fail("服务器内部异常，请稍后重试");
    }
}