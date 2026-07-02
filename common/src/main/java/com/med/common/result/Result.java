package com.med.common.result;

import lombok.Data;

/**
 * 全局统一JSON返回封装
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 成功静态方法
    public static <T> Result<T> success() {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        return r;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    // 失败静态方法
    public static <T> Result<T> fail() {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg("操作失败");
        return r;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}