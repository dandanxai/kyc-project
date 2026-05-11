package com.kyc.model.dto.enterprise;

import lombok.Data;

/**
 * 企业基本信息更新载荷
 */
@Data
public class EnterpriseUpdateDTO {
    private String enterpriseName;
    private String legalPerson;
    private String industry;
    private String scale;
    private String nature;
    private String logo;
    private String licenseUrl;
    private String contactName;
    private String contactPhone;
    private String email;
    private String introduction;
}