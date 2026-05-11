package com.kyc.config;

import com.kyc.interceptor.AdminAuthInterceptor;
import com.kyc.interceptor.PortalAuthInterceptor;
import com.kyc.interceptor.EnterpriseAuthInterceptor; // 🎯 1. 引入企业拦截器
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: kyc-api
 * @description: 跨域与多模块安全拦截器配置
 * @author: 黄胜
 * @create: 2026-05-08 12:04
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @Autowired
    private PortalAuthInterceptor portalAuthInterceptor;

    @Autowired
    private EnterpriseAuthInterceptor enterpriseAuthInterceptor; // 🎯 2. 注入企业拦截器

    /**
     * 1. 跨域安全配置 (CORS)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 2. 路由安全拦截器注册 (构建三大防线)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 🛡️ 防线一：管理端（Admin）安全拦截
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/auth/login");

        // 🛡️ 防线二：用户端/求职者门户（Portal）安全拦截
        registry.addInterceptor(portalAuthInterceptor)
                .addPathPatterns("/api/portal/**")
                .excludePathPatterns(
                        "/api/portal/auth/login",     // 放行个人登录
                        "/api/portal/auth/register"   // 放行个人注册
                );

        // 🛡️ 防线三：企业端（Enterprise）专属安全拦截
        registry.addInterceptor(enterpriseAuthInterceptor)
                .addPathPatterns("/api/enterprise/**") // 🎯 3. 拦截所有企业端专属接口
                .excludePathPatterns(
                        "/api/enterprise/user/login",    // 放行企业登录
                        "/api/enterprise/user/register"  // 放行企业注册
                );
    }
}