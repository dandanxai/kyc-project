package com.kyc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: kyc-api
 * @description: 企业用户实体类
 * @author: 黄胜
 * @create: 2026-05-11 14:00
 **/
@Data
@TableName("kyc_enterprise_user") // 🎯 对应达梦数据库企业表
public class EnterpriseUser {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String enterpriseName;
    private String legalPerson;
    private String enterpriseCode;
    private String industry;
    private String scale;
    private String nature;
    private String logo;
    private String licenseUrl;
    private String contactName;
    private String contactPhone;
    private String email;
    private String introduction;
    private Integer status; // 0-待审核，1-正常，2-审核不通过，3-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}