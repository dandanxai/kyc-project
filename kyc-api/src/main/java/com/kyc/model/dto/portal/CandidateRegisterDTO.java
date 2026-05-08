package com.kyc.model.dto.portal;

import lombok.Data;

/**
 * @program: kyc-api
 * @description: 灵活注册传输对象
 * @author: 黄胜
 * @create: 2026-05-08 14:11
 **/


@Data
public class CandidateRegisterDTO {
    private String phone;    // 手机号注册时传入
    private String email;    // 邮箱注册时传入
    private String password; // 必填密码
}