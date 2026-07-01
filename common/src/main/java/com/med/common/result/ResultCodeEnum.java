package com.med.common.result;

/**
 * 业务状态码枚举
 */
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(500, "服务器内部异常"),
    UNAUTHORIZED(401, "未登录或token失效"),
    FORBIDDEN(403, "权限不足"),
    PARAM_ERROR(400, "参数校验失败");

    private Integer code;
    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}