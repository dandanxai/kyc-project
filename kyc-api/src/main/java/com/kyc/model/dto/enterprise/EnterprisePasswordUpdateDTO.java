package com.kyc.model.dto.enterprise;

import lombok.Data;

/**
 * 企业修改密码安全载荷
 */
@Data
public class EnterprisePasswordUpdateDTO {
    private String currentPassword;
    private String newPassword;
}