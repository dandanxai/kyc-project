package com.kyc.model.dto.enterprise;

import lombok.Data;

/**
 * 企业灵活注册传输对象
 */
@Data
public class EnterpriseRegisterDTO {
    private String username;        // 登录账号
    private String password;        // 登录密码
    private String enterpriseName;  // 企业全称
    private String legalPerson;     // 法人
    private String enterpriseCode;  // 统一社会信用代码
    private String contactName;     // 联系人
    private String contactPhone;    // 联系电话
    private String email;           // 企业/联系邮箱
}