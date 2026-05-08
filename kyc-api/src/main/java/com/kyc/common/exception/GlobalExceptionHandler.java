package com.kyc.common.exception;

import com.kyc.common.api.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: kyc-api
 * @description: 全局安全异常拦截器
 * @author: 黄胜
 * @create: 2026-05-08 12:15
 **/


@RestControllerAdvice // 拦截所有 Controller 抛出的异常
public class GlobalExceptionHandler {

    /**
     * 1. 拦截业务中手动抛出的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult<Void> handleRuntimeException(RuntimeException e) {
        // 打印错误日志，方便我们在控制台定位 Bug
        System.err.println("【系统业务异常捕获】: " + e.getMessage());
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 2. 拦截系统未知的兜底异常 (如空指针、数据库崩了等)
     * 绝对不向前端暴露堆栈信息，只提供友好提示
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e) {
        e.printStackTrace(); // 在后端控制台打印堆栈，便于排查
        return CommonResult.failed("系统繁忙或发生未知错误，请联系系统安全管理员！");
    }
}