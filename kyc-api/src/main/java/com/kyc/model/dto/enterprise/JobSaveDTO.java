package com.kyc.model.dto.enterprise;

import lombok.Data;

/**
 * 新增/修改职位的数据载荷
 */
@Data
public class JobSaveDTO {
    private String title;                         // 职位名称 (如: Java开发工程师)
    private JobRequirementSpec requirementSpec;   // 动态需求规格对象

    // 原始文档元数据 (AI 解析或上传时携带，非必填)
    private String docName;
    private String docPath;
    private String docType;
    private Long docSize;

    private String tempFilePath;
}