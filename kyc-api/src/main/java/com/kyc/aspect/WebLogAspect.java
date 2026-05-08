package com.kyc.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * @program: kyc-api
 * @description: 全局请求接口日志切面
 * @author: 黄胜
 * @create: 2026-05-08 15:10
 **/
@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 🎯 定义切点：拦截 com.kyc.controller 包及其子包下的所有 Controller 方法
     */
    @Pointcut("execution(* com.kyc.controller..*Controller.*(..))")
    public void webLog() {}

    /**
     * 环绕通知：记录请求前后的所有细节
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 1. 获取当前请求的 Request 对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String url = "";
        String method = "";
        String ip = "";
        if (request != null) {
            url = request.getRequestURL().toString();
            method = request.getMethod();
            ip = request.getRemoteAddr();
        }

        // 2. 获取调用的类名和方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        // 3. 序列化入参（过滤掉 HttpServletRequest 等无法序列化的参数）
        Object[] args = joinPoint.getArgs();
        String params = "";
        try {
            // 过滤掉不可序列化的参数，避免报错
            Object[] filterArgs = Arrays.stream(args)
                    .filter(arg -> !(arg instanceof jakarta.servlet.ServletRequest)
                            && !(arg instanceof jakarta.servlet.ServletResponse)
                            && !(arg instanceof org.springframework.web.multipart.MultipartFile))
                    .toArray();
            params = objectMapper.writeValueAsString(filterArgs);
        } catch (Exception e) {
            params = Arrays.toString(args);
        }

        // 🎯 打印请求信息
        log.info("================  🚀 Request Start  ================ ");
        log.info("【请求 URL】: {} [{}]", url, method);
        log.info("【客户端 IP】: {}", ip);
        log.info("【调用方法】: {}.{}()", className, methodName);
        log.info("【请求参数】: {}", params);
        log.info("--------------------------------------------------- ");

        Object result = null;
        try {
            // 执行具体的业务方法
            result = joinPoint.proceed();
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            String resultStr = "";
            try {
                resultStr = objectMapper.writeValueAsString(result);
            } catch (Exception e) {
                resultStr = String.valueOf(result);
            }

            // 🎯 打印响应信息与执行耗时
            log.info("【返回数据】: {}", resultStr);
            log.info("【接口耗时】: {} ms", executionTime);
            log.info("================  🏁 Request End    ================ \n");
        }
    }
}