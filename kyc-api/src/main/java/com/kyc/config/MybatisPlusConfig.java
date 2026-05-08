package com.kyc.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kyc-api
 * @description: MyBatis-Plus 动态表名处理器
 * @author: 黄胜
 * @create: 2026-05-08 14:49
 **/


/**
 * MyBatis-Plus 拦截器配置（完美适配达梦小写表名）
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 🎯 核心黑魔法：动态表名处理器
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            // 只要表名没有被双引号包裹，就自动帮它加上双引号发送给达梦
            if (!tableName.startsWith("\"") && !tableName.endsWith("\"")) {
                return "\"" + tableName + "\"";
            }
            return tableName;
        });

        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }
}