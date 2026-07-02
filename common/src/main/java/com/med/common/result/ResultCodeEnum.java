package com.med.common.result;

import lombok.Getter;

/**
 * 全局业务状态码枚举
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),
    FAIL(500, "系统异常"),

    // 登录认证相关
    TOKEN_NULL(401, "未登录，请先登录"),
    TOKEN_INVALID(401, "Token无效或已过期"),
    PERMISSION_DENIED(403, "权限不足，禁止访问"),

    // 业务通用
    DATA_NOT_FOUND(404, "数据不存在"),
    USER_EXIST(400, "用户名已存在"),
    PWD_ERROR(400, "账号或密码错误");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}