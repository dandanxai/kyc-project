package com.kyc.common.api;

import lombok.Data;

/**
 * @program: kyc-api
 * @description: 通用 API 返回规范
 * @author: 黄胜
 * @create: 2026-05-08 12:05
 **/


@Data
public class CommonResult<T> {
    private Integer code;     // 状态码：200 成功，400 参数错误，401 凭证失效，403 无权访问，500 系统防线拦截
    private String message;   // 安全友好的提示信息
    private T data;           // 脱敏后的业务数据

    protected CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(200, message, data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(500, message, null);
    }

    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<>(400, message, null);
    }

    public static <T> CommonResult<T> unauthorized(String message) {
        return new CommonResult<>(401, message, null);
    }

    public static <T> CommonResult<T> forbidden(String message) {
        return new CommonResult<>(403, message, null);
    }
}