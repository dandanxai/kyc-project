package com.kyc.common.context;

/**
 * 🎯 企业本地变量：安全平替 Spring Security 的上下文持有者
 */
public class EnterpriseContext {
    private static final ThreadLocal<Long> ENTERPRISE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setEnterpriseId(Long enterpriseId) {
        ENTERPRISE_THREAD_LOCAL.set(enterpriseId);
    }

    public static Long getEnterpriseId() {
        return ENTERPRISE_THREAD_LOCAL.get();
    }

    public static void clear() {
        ENTERPRISE_THREAD_LOCAL.remove();
    }
}