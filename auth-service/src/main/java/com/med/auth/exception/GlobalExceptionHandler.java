package com.med.auth.exception;

import com.med.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 新增：专门捕获自定义业务异常，返回自定义提示
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMsg());
        return Result.fail(e.getMsg());
    }

    /**
     * 参数为空、非法参数异常统一处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgException(IllegalArgumentException e) {
        log.error("参数异常：{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    /**
     * 兜底捕获所有未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统未知异常：", e);
        return Result.fail("服务器内部异常，请稍后重试");
    }
}