package com.kyc.controller.enterprise;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyc.common.api.CommonResult;
import com.kyc.common.context.EnterpriseContext;
import com.kyc.entity.Job;
import com.kyc.model.dto.enterprise.JobSaveDTO;
import com.kyc.service.JobService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: kyc-api
 * @description: 企业后台职位维护控制器
 * @author: 黄胜
 * @create: 2026-05-11 17:45
 **/
@RestController
@RequestMapping("/api/enterprise/job")
@CrossOrigin // 解决前端本地调试跨域
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 1. 发布新职位
     */
    @PostMapping("/create")
    public CommonResult<Void> createJob(@RequestBody JobSaveDTO jobSaveDTO) {
        try {
            jobService.saveJob(jobSaveDTO);
            return CommonResult.success(null, "职位发布成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("职位发布失败");
        }
    }

    /**
     * 2. 更新指定职位信息
     */
    @PutMapping("/update/{id}")
    public CommonResult<Void> updateJob(@PathVariable("id") Long jobId, @RequestBody JobSaveDTO jobSaveDTO) {
        try {
            jobService.updateJob(jobId, jobSaveDTO);
            return CommonResult.success(null, "职位信息修改成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("职位修改失败");
        }
    }

    /**
     * 3. 物理删除指定职位
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult<Void> deleteJob(@PathVariable("id") Long jobId) {
        try {
            jobService.deleteJob(jobId);
            return CommonResult.success(null, "职位已成功永久删除");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("职位删除失败");
        }
    }

    /**
     * 4. 分页检索企业当前发布的所有岗位
     */
    @GetMapping("/page")
    public CommonResult<Page<Job>> getJobPage(
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) Integer status) {
        try {
            Page<Job> jobPage = jobService.listJobsByPage(current, size, status);
            return CommonResult.success(jobPage, "获取职位列表成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取职位列表失败");
        }
    }

    /**
     * 5. 查看单个职位详情
     */
    @GetMapping("/detail/{id}")
    public CommonResult<Job> getJobDetail(@PathVariable("id") Long jobId) {
        try {
            Job job = jobService.getJobDetail(jobId);
            return CommonResult.success(job, "获取职位详情成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取职位详情失败");
        }
    }

    /**
     * 6. 岗位文档/附件临时上传暂存
     * 接口：POST /api/enterprise/job/temp-upload
     */
    @PostMapping("/temp-upload")
    public CommonResult<Map<String, String>> tempUpload(@RequestParam("file") MultipartFile file) {
        try {
            Long enterpriseId = EnterpriseContext.getEnterpriseId();
            if (enterpriseId == null) {
                return CommonResult.unauthorized("未检测到有效企业登录状态");
            }

            if (file.isEmpty()) {
                return CommonResult.validateFailed("上传文件不能为空");
            }

            // 调用 Service 进行暂存，返回该文件的相对/临时路径
            String tempPath = jobService.saveTempFile(file, enterpriseId);

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("tempPath", tempPath);
            // 🎯 统一规范：将 /temp/preview 改为 /temp-preview，使其与 GetMapping 完美匹配
            resultMap.put("previewUrl", "/api/enterprise/job/temp-preview?path=" + tempPath);

            return CommonResult.success(resultMap, "文件已成功在服务器暂存");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("文件暂存失败: " + e.getMessage());
        }
    }

    /**
     * 7. 岗位文档/附件临时预览流获取 (支持 PDF / DOCX 物理流返回)
     * 接口：GET /api/enterprise/job/temp-preview
     */
    @GetMapping("/temp-preview")
    public void previewTempFile(@RequestParam("path") String filePath, HttpServletResponse response) {
        try {
            // 1. 安全过滤：防止恶意前端利用 "../" 越界访问敏感文件
            if (filePath.contains("..") || filePath.startsWith("/") || filePath.startsWith("\\")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // 2. 定位物理文件
            // 🎯 使用绝对路径定位，确保多模块部署或 IDE 运行路径偏移时依然精准
            File file = new File("uploads/temp/" + filePath);

            // 如果文件不存在，打印系统当前运行的绝对路径，帮你在控制台揪出「文件到底存哪了」
            if (!file.exists() || !file.isFile()) {
                System.err.println("【文件预览失败】找不到物理文件！");
                System.err.println("👉 传入的文件名: " + filePath);
                System.err.println("👉 后端尝试读取的绝对路径: " + file.getAbsolutePath());
                System.err.println("👉 请检查该路径下是否存在该文件，或者检查上传接口(JobServiceImpl)是不是把文件存在了别的地方。");

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 3. 根据后缀动态设置 Content-Type
            String fileNameLower = filePath.toLowerCase();
            if (fileNameLower.endsWith(".pdf")) {
                response.setContentType("application/pdf");
            } else if (fileNameLower.endsWith(".docx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            } else {
                response.setContentType("application/octet-stream");
            }

            // 4. 标准响应头设置
            response.setContentLengthLong(file.length());

            // 5. 管道双向对拷，安全刷入前端
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}