package com.kyc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyc.entity.CandidateResume;
import com.kyc.mapper.CandidateResumeMapper;
import com.kyc.service.CandidateResumeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @program: kyc-api
 * @description: 简历管理服务实现类
 * @author: 黄胜
 * @create: 2026-05-10 11:30
 **/
@Service
public class CandidateResumeServiceImpl extends ServiceImpl<CandidateResumeMapper, CandidateResume> implements CandidateResumeService {

    @Value("${kyc.upload.resume-path:uploads/resumes/}")
    private String uploadFolder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CandidateResume uploadResume(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new RuntimeException("无效的文件名");
        }

        // 🎯 核心防重：拦截当前用户下已存在的同名文件
        Long count = lambdaQuery()
                .eq(CandidateResume::getUserId, userId)
                .eq(CandidateResume::getFileName, originalFilename)
                .count();

        if (count > 0) {
            // 抛出特定的业务异常，由统一异常处理器或 Controller 捕获并友好返回给前端
            throw new RuntimeException("已上传过同名文件：" + originalFilename);
        }

        // 创建存储目录
        File folder = new File(uploadFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 提取后缀
        String fileType = "";
        if (originalFilename.contains(".")) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        // 生成服务器端的物理文件名（UUID防物理重名覆盖）
        String saveFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        File destFile = new File(folder, saveFileName);

        try {
            // 保存物理文件
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException("物理文件写入磁盘失败: " + e.getMessage());
        }

        // 默认将之前的所有简历设为非默认（如果当前要设为默认的话，这里保持系统默认策略）
        // 体验优化：若这是该用户的第 1 份简历，设为默认(1)；否则默认设为历史备份(0)
        Long existingCount = lambdaQuery().eq(CandidateResume::getUserId, userId).count();
        int isActiveValue = (existingCount == 0) ? 1 : 0;

        // 封装实体存入达梦数据库
        CandidateResume resume = new CandidateResume();
        resume.setUserId(userId);
        resume.setFileName(originalFilename);
        resume.setFilePath(destFile.getAbsolutePath());
        resume.setFileType(fileType);
        resume.setFileSize(file.getSize());
        resume.setIsActive(isActiveValue);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdateTime(LocalDateTime.now());

        this.save(resume);

        return resume;
    }

    @Override
    public List<CandidateResume> getResumeList(Long userId) {
        return lambdaQuery()
                .eq(CandidateResume::getUserId, userId)
                .orderByDesc(CandidateResume::getCreateTime)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultResume(Long userId, Long resumeId) {
        // 1. 越权校验：确保修改的是属于自己的简历
        CandidateResume target = getById(resumeId);
        if (target == null || !target.getUserId().equals(userId)) {
            throw new RuntimeException("目标简历不存在或无权操作");
        }

        // 2. 将当前用户的所有简历状态重置为 0
        lambdaUpdate()
                .eq(CandidateResume::getUserId, userId)
                .set(CandidateResume::getIsActive, 0)
                .update();

        // 3. 将目标简历激活
        lambdaUpdate()
                .eq(CandidateResume::getId, resumeId)
                .set(CandidateResume::getIsActive, 1)
                .set(CandidateResume::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResume(Long userId, Long resumeId) {
        CandidateResume target = getById(resumeId);
        if (target == null || !target.getUserId().equals(userId)) {
            throw new RuntimeException("简历不存在，无法删除");
        }

        // 1. 删除磁盘物理文件
        File file = new File(target.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // 2. 从达梦中擦除
        this.removeById(resumeId);

        // 3. 体验增强：如果删掉的是当前激活的简历，且用户还有其他简历，则自动将最新的一条置为默认
        if (target.getIsActive() == 1) {
            List<CandidateResume> remaining = lambdaQuery()
                    .eq(CandidateResume::getUserId, userId)
                    .orderByDesc(CandidateResume::getCreateTime)
                    .list();
            if (!remaining.isEmpty()) {
                CandidateResume latest = remaining.get(0);
                lambdaUpdate()
                        .eq(CandidateResume::getId, latest.getId())
                        .set(CandidateResume::getIsActive, 1)
                        .set(CandidateResume::getUpdateTime, LocalDateTime.now())
                        .update();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResumes(Long userId, List<Long> resumeIds) {
        if (resumeIds == null || resumeIds.isEmpty()) {
            return;
        }

        List<CandidateResume> targets = lambdaQuery()
                .eq(CandidateResume::getUserId, userId)
                .in(CandidateResume::getId, resumeIds)
                .list();

        if (targets.isEmpty()) {
            throw new RuntimeException("所选简历不存在或无操作权限");
        }

        boolean hasActiveDeleted = false;

        for (CandidateResume target : targets) {
            File file = new File(target.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            if (target.getIsActive() == 1) {
                hasActiveDeleted = true;
            }
        }

        this.removeByIds(resumeIds);

        if (hasActiveDeleted) {
            List<CandidateResume> remaining = lambdaQuery()
                    .eq(CandidateResume::getUserId, userId)
                    .orderByDesc(CandidateResume::getCreateTime)
                    .list();
            if (!remaining.isEmpty()) {
                CandidateResume latest = remaining.get(0);
                lambdaUpdate()
                        .eq(CandidateResume::getId, latest.getId())
                        .set(CandidateResume::getIsActive, 1)
                        .set(CandidateResume::getUpdateTime, LocalDateTime.now())
                        .update();
            }
        }
    }
}
