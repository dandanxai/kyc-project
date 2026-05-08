package com.kyc.model.dto.portal;

import lombok.Data;

/**
 * @program: kyc-api
 * @description: 统一登录传输对象
 * @author: 黄胜
 * @create: 2026-05-08 14:11
 **/


@Data
public class LoginDTO {
    private String account;  // 🎯 支持手机号或邮箱
    private String password;
}