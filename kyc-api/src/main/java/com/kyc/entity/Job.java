package com.kyc.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kyc.model.dto.enterprise.JobRequirementSpec;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: kyc-api
 * @description: 企业职位实体类（无逻辑删除，直接物理删除）
 * @author: 黄胜
 * @create: 2026-05-11 17:30
 **/
@Data
@TableName(value = "kyc_job", autoResultMap = true) // 🎯 必须加上 autoResultMap = true 才能使 TypeHandler 正常生效
public class Job {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布该职位的企业用户ID
     */
    private Long enterpriseId;

    /**
     * 职位名称
     */
    private String title;

    /**
     * 🎯 指定 JacksonTypeHandler 自动完成 JSON 序列化与反序列化
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JobRequirementSpec requirementSpec;

    // ==================== 📁 原始文档元数据 ====================

    /**
     * 原始文件名称
     */
    private String docName;

    /**
     * 文件存储相对路径或 OSS URL
     */
    private String docPath;

    /**
     * 文件扩展名 (pdf, docx 等)
     */
    private String docType;

    /**
     * 文件大小 (Byte)
     */
    private Long docSize;

    // ==================== ⚙️ 状态与审计字段 ====================

    /**
     * 职位状态：0-待确认/解析中，1-招聘中，2-已下线
     */
    private Integer status;

    /**
     * 职位创建时间（对应上传时间）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 职位更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}