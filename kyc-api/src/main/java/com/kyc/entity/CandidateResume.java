package com.kyc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @program: kyc-api
 * @description: 候选人简历实体类
 * @author: 黄胜
 * @create: 2026-05-10 11:20
 **/
@Data
@TableName("kyc_candidate_resume") // 🎯 对应达梦建表 DDL 中的表名
public class CandidateResume {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的用户（求职者）ID
     */
    private Long userId;

    /**
     * 原始文件名（如：张三_前端开发简历.pdf）
     */
    private String fileName;

    /**
     * 物理存储路径或相对 URL 路径
     */
    private String filePath;

    /**
     * 文件格式后缀（如：pdf, docx）
     */
    private String fileType;

    /**
     * 文件大小（单位：字节 Byte）
     */
    private Long fileSize;

    /**
     * 是否为当前投递使用的默认简历：1-激活，0-历史备份
     */
    private Integer isActive;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
