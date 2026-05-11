package com.kyc.model.dto.enterprise;

import lombok.Data;

/**
 * 企业统一登录传输对象
 */
@Data
public class EnterpriseLoginDTO {
    private String username;  // 🎯 支持账号/社会信用代码/手机号登录
    private String password;
}