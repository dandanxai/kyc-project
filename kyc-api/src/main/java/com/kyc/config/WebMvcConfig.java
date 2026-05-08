package com.kyc.config;

import com.kyc.interceptor.AdminAuthInterceptor;
import com.kyc.interceptor.PortalAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: kyc-api
 * @description: 跨域安全拦截器
 * @author: 黄胜
 * @create: 2026-05-08 12:04
 **/


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @Autowired
    private PortalAuthInterceptor portalAuthInterceptor;

    /**
     * 1. 跨域安全配置 (CORS)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 在生产环境下，这里应配置为具体的前端域名（如 kyc.yourdomain.com）
                // 本地联调阶段，我们允许来自前端开发服务器的受信任跨域请求
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 预检请求有效期为1小时，避免高频预检影响性能
    }

    /**
     * 2. 路由安全拦截器注册 (核心防线)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 🛡️ 防线一：管理端（Admin）安全拦截
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/admin/**")               // 拦截所有管理端接口
                .excludePathPatterns("/api/admin/auth/login");  // 放行后台登录接口

        // 🛡️ 防线二：用户端/门户（Portal）安全拦截
        registry.addInterceptor(portalAuthInterceptor)
                .addPathPatterns("/api/portal/**")              // 拦截所有用户端接口
                // 放行用户端的注册、登录、公开大厅展示等非敏感接口
                .excludePathPatterns(
                        "/api/portal/auth/login",
                        "/api/portal/auth/register",
                        "/api/portal/enterprise/active-cards"
                );
    }
}