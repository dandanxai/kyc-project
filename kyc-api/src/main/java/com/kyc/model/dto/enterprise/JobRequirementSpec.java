package com.kyc.model.dto.enterprise;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 岗位需求规格 (映射到数据库中的 JSON 字段)
 */
@Data
public class JobRequirementSpec {
    private Map<String, Object> salary;       // 薪资待遇 (如: {"min": 8000, "max": 12000, "text": "8K-12K"})
    private String workLocation;              // 工作地点 (如: 合肥)
    private String experienceRequirement;     // 经验要求 (如: 1-3年 / 应届生)
    private String educationRequirement;      // 学历要求 (如: 大专 / 本科)
    private List<String> jobDescription;      // 岗位职责列表 (AI解析或手动输入)
    private List<String> jobRequirement;      // 任职要求列表 (AI解析或手动输入)
    private List<String> skills;              // 技能标签 (如: ["Java", "Vue", "Spring"])
    private List<String> welfare;             // 福利标签 (如: ["五险一金", "双休"])

    // 额外扩展属性，支持未来不同行业特有属性的灵活扩展
    private Map<String, Object> extraAttributes;
}