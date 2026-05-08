package com.kyc.model.dto.portal;

import lombok.Data;

/**
 * @program: kyc-api
 * @description: 基本信息更新载荷
 * @author: 黄胜
 * @create: 2026-05-08 15:45
 **/


@Data
public class CandidateUpdateDTO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String jobStatus;
}