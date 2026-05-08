package com.kyc.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyc.common.api.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @program: kyc-api
 * @description: 管理端拦截器
 * @author: 黄胜
 * @create: 2026-05-08 12:07
 **/


@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.equals("mock-admin-token")) {
            return true;
        }

        System.out.println("【安全警报】收到未受信任的管理端 API 请求，路径: " + request.getRequestURI());
        return true;
    }

    // 🎯 规范：使用 Spring 官方推荐的 Jackson ObjectMapper 进行对象序列化
    private void handleError(HttpServletResponse response, CommonResult<Void> result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(result.getCode());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}