package com.kyc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: kyc-api
 * @description: 个人用户实体类
 * @author: 黄胜
 * @create: 2026-05-08 14:05
 **/


@Data
@TableName("kyc_candidate") // 🎯 必须对应 kyc_ 前缀的表名
public class Candidate {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;  // 🎯 补上我们在数据库新增的 email
    private String avatar;
    private Integer status; // 🎯 对应 INT DEFAULT 0
    private String role;
    private String jobStatus; // MyBatis-Plus 会自动将下划线 job_status 转为驼峰 jobStatus
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}