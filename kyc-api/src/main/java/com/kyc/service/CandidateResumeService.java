package com.kyc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyc.entity.CandidateResume;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @program: kyc-api
 * @description: 简历管理服务接口
 * @author: 黄胜
 * @create: 2026-05-10 11:25
 **/
public interface CandidateResumeService extends IService<CandidateResume> {

    /**
     * 上传/保存个人简历文件（支持 PDF, DOC, DOCX 格式）
     * @param userId 当前求职者 ID
     * @param file 前端上传的多媒体文件对象
     * @return 保存成功后的简历记录
     */
    CandidateResume uploadResume(Long userId, MultipartFile file);

    /**
     * 获取当前用户的全部简历列表（支持历史版本展示）
     * @param userId 用户 ID
     * @return 简历列表
     */
    List<CandidateResume> getResumeList(Long userId);

    /**
     * 切换某份简历为默认投递简历
     * @param userId 用户 ID
     * @param resumeId 简历主键 ID
     */
    void setDefaultResume(Long userId, Long resumeId);

    /**
     * 删除指定的简历（同时清理本地磁盘物理文件）
     * @param userId 用户 ID
     * @param resumeId 简历主键 ID
     */
    void deleteResume(Long userId, Long resumeId);

    /**
     * 批量删除指定的简历（同时清理本地磁盘物理文件）
     * @param userId 用户 ID
     * @param resumeIds 简历主键 ID 集合
     */
    void deleteResumes(Long userId, List<Long> resumeIds);
}
