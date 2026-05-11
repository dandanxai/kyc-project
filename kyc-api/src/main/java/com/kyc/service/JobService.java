package com.kyc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kyc.entity.Job;
import com.kyc.model.dto.enterprise.JobSaveDTO;
import org.springframework.web.multipart.MultipartFile;

public interface JobService extends IService<Job> {

    /**
     * 发布/保存新职位
     */
    void saveJob(JobSaveDTO jobSaveDTO);

    /**
     * 修改现有职位
     */
    void updateJob(Long jobId, JobSaveDTO jobSaveDTO);

    /**
     * 物理删除职位（删除就是彻底删除，且只能删除自己企业的职位）
     */
    void deleteJob(Long jobId);

    /**
     * 分页查询当前登录企业的职位列表
     */
    Page<Job> listJobsByPage(int current, int size, Integer status);

    /**
     * 获取当前企业下某个职位的详细信息
     */
    Job getJobDetail(Long jobId);

    /**
     * 🎯 新增：暂存岗位附件文件
     */
    String saveTempFile(MultipartFile file, Long enterpriseId);
}