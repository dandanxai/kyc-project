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
 * @description: 企业端安全拦截器（确保只有企业角色能访问企业后台）
 * @author: 黄胜
 **/
@Component
public class EnterpriseAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil; // 注入 JWT 工具类解析 Token

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行跨域预检请求（OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 从请求头中获取 Authorization Token
        String token = request.getHeader("Authorization");

        // 🎯 3. 核心防御：严格防空、防前端 "null"、"undefined" 脏字符串
        if (!StringUtils.hasText(token) || "null".equalsIgnoreCase(token.trim()) || "undefined".equalsIgnoreCase(token.trim())) {
            CommonResult<Void> errorResult = CommonResult.unauthorized("企业账户登录已过期或无权访问，请重新登录");
            handleError(response, errorResult);
            return false;
        }

        // 🎯 4. 增加 try-catch 包裹，防止 JWT 解析库在遇到失效/受损 Token 时崩溃抛出 500
        try {
            // 解析 Token 并验证是否过期
            Claims claims = jwtUtil.parseToken(token);
            if (claims != null) {
                String userId = (String) claims.get("userId");
                String userType = (String) claims.get("userType");

                // 🔴 核心权限锁：确保是企业用户（enterprise）访问
                if ("enterprise".equals(userType)) {
                    // 将当前登录企业 ID 存入 request 作用域中
                    request.setAttribute("currentUserId", userId);
                    // 写入上下文（注意：之前你这里错写成了 UserContext，企业端建议写入 EnterpriseContext 防止线程变量污染）
                    com.kyc.common.context.EnterpriseContext.setEnterpriseId(Long.parseLong(userId));
                    return true; // 🎯 盘查通过，放行！
                }
            }
        } catch (Exception e) {
            // 捕获任何可能因为 Token 格式损坏、过期、空指针引发的解析异常
            CommonResult<Void> errorResult = CommonResult.unauthorized("身份令牌已失效，请重新登录");
            handleError(response, errorResult);
            return false;
        }

        // 5. 盘查失败，返回标准的 401 未授权
        CommonResult<Void> errorResult = CommonResult.unauthorized("企业账户登录已过期或无权访问，请重新登录");
        handleError(response, errorResult);
        return false;
    }

    // 规范序列化
    private void handleError(HttpServletResponse response, CommonResult<Void> result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 状态码

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 🎯 请求结束后，务必及时清理 ThreadLocal 内存，防止内存泄漏和线程复用污染
        com.kyc.common.context.EnterpriseContext.clear();
    }
}