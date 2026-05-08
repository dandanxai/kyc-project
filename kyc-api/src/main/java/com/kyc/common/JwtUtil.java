package com.kyc.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets; // 🎯 引入标准字符集
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 令牌工具类
 * 配置项已全面解耦至 application.yml
 */
@Component
public class JwtUtil {

    // 动态注入 yml 中的密钥
    @Value("${kyc.jwt.secret-key:kyc_default_secret_key_backup_2026}")
    private String secretKey;

    // 动态注入 yml 中的过期时间
    @Value("${kyc.jwt.expiration:604800000}")
    private long expirationTime;

    /**
     * 生成 JWT Token
     */
    public String createToken(String userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);

        // 🎯 核心修复点：将 secretKey 转换为 byte[] 字节数组传入，防止 jjwt 把它当 Base64 解码！
        byte[] apiKeySecretBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Jwts.builder()
                .setClaims(claims) // 载荷
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, apiKeySecretBytes) // 🎯 传入字节数组
                .compact();
    }

    /**
     * 解析 JWT Token
     */
    public Claims parseToken(String token) {
        try {
            // 🎯 核心修复点：解析时同样使用 byte[] 字节数组进行验签
            byte[] apiKeySecretBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            return Jwts.parser()
                    .setSigningKey(apiKeySecretBytes) // 🎯 传入字节数组
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 打印出具体解析失败的原因，方便调试
            System.err.println("JWT 解析失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 校验 Token 是否已经过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        return claims.getExpiration().before(new Date());
    }

    /**
     * 从 Token 中快速提取出用户 ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("userId") : null;
    }

    /**
     * 从 Token 中快速提取出用户身份类型
     */
    public String getUserTypeFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("userType") : null;
    }
}