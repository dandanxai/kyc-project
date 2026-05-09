package com.kyc.common.context;

/**
 * @program: kyc-api
 * @description: 利用 ThreadLocal 存储当前线程（即当前请求）解析出来的用户 ID
 * @author: 黄胜
 * @create: 2026-05-09 09:21
 **/


/**
 * 🎯 线程本地变量：安全平替 Spring Security 的上下文持有者
 */
public class UserContext {
    private static final ThreadLocal<Long> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_THREAD_LOCAL.set(userId);
    }

    public static Long getUserId() {
        return USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}