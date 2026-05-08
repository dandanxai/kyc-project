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

    private Long id;

    /**
     * 身份令牌（安全钥匙），前端后续所有请求都需要在 Request Header 中携带此 Token
     */
    private String token;

    /**
     * 登录账号（对应达梦数据库中的 username，注册时默认会同步设为手机号或邮箱）
     */
    private String username;

    /**
     * 用户昵称/真实姓名（刚注册时为 NULL，后续在个人中心完善）
     */
    private String nickname;

    /**
     * 用户头像 URL 地址（刚注册时为 NULL，后续完善）
     */
    private String avatar;

    /**
     * 绑定的电子邮箱（如果是手机注册，此时为 NULL，后续可绑定）
     */
    private String email;

    /**
     * 绑定的手机号码（如果是邮箱注册，此时为 NULL，后续可绑定）
     */
    private String phone;

    /**
     * 角色标识（默认：candidate 代表个人求职者）
     */
    private String role;
}