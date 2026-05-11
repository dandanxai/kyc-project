package com.kyc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyc.common.context.EnterpriseContext;
import com.kyc.entity.Job;
import com.kyc.mapper.JobMapper;
import com.kyc.model.dto.enterprise.JobSaveDTO;
import com.kyc.service.JobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 职位服务实现类 (无逻辑删除，直接物理删除)
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    // 🎯 所有的文件均统一放置在该根目录下
    @Value("${kyc.upload.temp-path:uploads/temp/}")
    private String BASE_UPLOAD_DIR;

    @Override
    public void saveJob(JobSaveDTO jobSaveDTO) {
        Long enterpriseId = EnterpriseContext.getEnterpriseId();
        if (enterpriseId == null) {
            throw new RuntimeException("未检测到有效企业登录状态");
        }

        if (!StringUtils.hasText(jobSaveDTO.getTitle())) {
            throw new RuntimeException("职位名称不能为空");
        }

        Job job = new Job();
        BeanUtils.copyProperties(jobSaveDTO, job);
        job.setEnterpriseId(enterpriseId);
        job.setStatus(1); // 默认直接上线

        // 🎯 极简逻辑：如果有暂存文件，直接将其路径（或文件名）绑定到 Job 的相应字段中即可，无需转移物理位置
        if (StringUtils.hasText(jobSaveDTO.getTempFilePath())) {
            // 假设 Job 实体中有对应的文件路径存储字段，例如 setAttachedPath
            // job.setAttachedPath(jobSaveDTO.getTempFilePath());
        }

        boolean success = save(job);
        if (!success) {
            throw new RuntimeException("职位发布失败，数据库写入异常");
        }
    }

    /**
     * 实现：保存文件到统一的根目录下 (BASE_UPLOAD_DIR)
     */
    @Override
    public String saveTempFile(MultipartFile file, Long enterpriseId) {
        try {
            // 1. 确保根目录存在
            File dir = new File(BASE_UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2. 规避文件名冲突：UUID + 原始后缀
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String safeFileName = UUID.randomUUID().toString() + fileExtension;

            // 3. 直接写入根目录下，不再创建子文件夹
            Path targetPath = Paths.get(BASE_UPLOAD_DIR, safeFileName);
            Files.write(targetPath, file.getBytes());

            // 4. 返回文件名（或相对路径），以便前端提交表单时原样带回
            return safeFileName;
        } catch (IOException e) {
            throw new RuntimeException("文件保存磁盘异常: " + e.getMessage());
        }
    }

    @Override
    public void updateJob(Long jobId, JobSaveDTO jobSaveDTO) {
        Long enterpriseId = EnterpriseContext.getEnterpriseId();

        // 1. 严格的安全盘查：确保该职位存在，且属于当前登录的企业，防止越权篡改
        Job exist = getById(jobId);
        if (exist == null || !exist.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权修改此职位");
        }

        // 2. 字段拷贝与更新
        Job updateEntity = new Job();
        BeanUtils.copyProperties(jobSaveDTO, updateEntity);
        updateEntity.setId(jobId);

        // 如果更新时携带了新的暂存文件，同样直接关联对应字段即可
        if (StringUtils.hasText(jobSaveDTO.getTempFilePath())) {
            // updateEntity.setAttachedPath(jobSaveDTO.getTempFilePath());
        }

        boolean success = updateById(updateEntity);
        if (!success) {
            throw new RuntimeException("职位修改失败，达梦数据库更新异常");
        }
    }

    @Override
    public void deleteJob(Long jobId) {
        Long enterpriseId = EnterpriseContext.getEnterpriseId();

        // 1. 安全校验：确认属于该企业
        Job exist = getById(jobId);
        if (exist == null || !exist.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权删除此职位");
        }

        // 2. 物理删除
        boolean success = removeById(jobId);
        if (!success) {
            throw new RuntimeException("职位删除失败，数据库擦除异常");
        }
    }

    @Override
    public Page<Job> listJobsByPage(int current, int size, Integer status) {
        Long enterpriseId = EnterpriseContext.getEnterpriseId();
        if (enterpriseId == null) {
            throw new RuntimeException("未检测到有效登录状态");
        }

        Page<Job> page = new Page<>(current, size);
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getEnterpriseId, enterpriseId);

        if (status != null) {
            queryWrapper.eq(Job::getStatus, status);
        }

        queryWrapper.orderByDesc(Job::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public Job getJobDetail(Long jobId) {
        Long enterpriseId = EnterpriseContext.getEnterpriseId();

        Job job = getById(jobId);
        if (job == null || !job.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权查看");
        }
        return job;
    }
}