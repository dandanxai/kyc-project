package com.kyc.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyc.common.JwtUtil;
import com.kyc.common.api.CommonResult;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @program: kyc-api
 * @description: 用户端拦截器（接入真实 JWT 安全验证）
 * @author: 黄胜
 * @create: 2026-05-08 12:08
 **/
@Component
public class PortalAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil; // 🎯 注入刚刚写好的 JWT 工具类

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行跨域预检请求（OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 从请求头中获取 Authorization Token
        String token = request.getHeader("Authorization");

        // 3. 开始安全性盘查
        if (StringUtils.hasText(token)) {
            // 解析 Token 并验证是否过期
            Claims claims = jwtUtil.parseToken(token);

            if (claims != null && !jwtUtil.isTokenExpired(token)) {
                // 4. 盘查通过！从 Token 中提取出用户 ID 和 角色类型
                String userId = (String) claims.get("userId");
                String userType = (String) claims.get("userType");

                // 5. 确保是个人用户（candidate）访问用户端，防止身份越权
                if ("candidate".equals(userType)) {
                    // 将当前登录用户 ID 存入 request 作用域中，方便后续 Controller 直接获取
                    request.setAttribute("currentUserId", userId);
                    return true; // 🎯 放行请求！
                }
            }
        }

        // 6. 盘查失败，使用你写的 handleError 规范返回 401（未授权/登录过期）
        // 这里的 401 可以对应你们项目里 CommonResult 的状态码设计
        CommonResult<Void> errorResult = CommonResult.unauthorized("您的登录已过期，请重新登录");
        handleError(response, errorResult);
        return false;
    }

    // 🎯 规范：使用 Spring 官方推荐的 Jackson ObjectMapper 进行对象序列化
    private void handleError(HttpServletResponse response, CommonResult<Void> result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        // 401 代表未授权（或者跟你的业务设计走，如设置为 200，在 Body 里放错误码）
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}