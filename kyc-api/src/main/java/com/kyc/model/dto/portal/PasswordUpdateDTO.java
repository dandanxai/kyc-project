package com.kyc.model.dto.portal;

import lombok.Data;

/**
 * @program: kyc-api
 * @description: 修改密码安全载荷
 * @author: 黄胜
 * @create: 2026-05-08 15:45
 **/


@Data
public class PasswordUpdateDTO {
    private Long id;
    private String currentPassword;
    private String newPassword;
}