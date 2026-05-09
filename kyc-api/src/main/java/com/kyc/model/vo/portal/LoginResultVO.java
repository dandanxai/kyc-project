package com.kyc.model.vo.portal;

/**
 * @program: kyc-api
 * @description: 登录注册返回对象
 * @author: 黄胜
 * @create: 2026-05-08 14:15
 **/


import lombok.Data;

/**
 * 个人用户登录成功后返回给前端的视图对象（VO）
 */
@Data
public class LoginResultVO {
    /**
     * 身份令牌（安全钥匙），前端后续所有请求都需要在 Request Header 中携带此 Token
     */
    private String token;
}