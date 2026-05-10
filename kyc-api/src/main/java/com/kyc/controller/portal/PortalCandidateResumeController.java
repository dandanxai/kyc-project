package com.kyc.controller.portal;

import com.kyc.common.api.CommonResult;
import com.kyc.common.context.UserContext;
import com.kyc.entity.CandidateResume;
import com.kyc.service.CandidateResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.File;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kyc-api
 * @description: 个人简历上传与生命周期管理控制器
 * @author: 黄胜
 * @create: 2026-05-10 11:40
 **/
@RestController
@RequestMapping("/api/portal/candidate/resume")
@CrossOrigin
public class PortalCandidateResumeController {

    @Autowired
    private CandidateResumeService candidateResumeService;

    private final RestTemplate restTemplate = new RestTemplate();


    /**
     * 1. 单个简历文件上传接口
     */
    @PostMapping("/upload")
    public CommonResult<CandidateResume> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = UserContext.getUserId();
            CandidateResume resume = candidateResumeService.uploadResume(userId, file);
            return CommonResult.success(resume, "简历上传成功");
        } catch (RuntimeException e) {
            // 🎯 拦截到已存在同名文件或格式错误，直接将错误信息打包返回
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("上传系统发生未知故障");
        }
    }

    /**
     * 5. 批量上传简历接口
     */
    @PostMapping("/upload-batch")
    public CommonResult<Void> uploadResumes(@RequestParam("files") MultipartFile[] files) {
        try {
            Long userId = UserContext.getUserId();
            StringBuilder duplicateNames = new StringBuilder();

            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        candidateResumeService.uploadResume(userId, file);
                    } catch (RuntimeException e) {
                        // 记录下批量上传中重名报错的文件，其他未重名的依旧可以成功上传
                        duplicateNames.append("[").append(file.getOriginalFilename()).append("] ");
                    }
                }
            }

            if (duplicateNames.length() > 0) {
                return CommonResult.validateFailed("部分文件因同名跳过未上传: " + duplicateNames.toString());
            }

            return CommonResult.success(null, "批量简历上传并开始分析");
        } catch (Exception e) {
            return CommonResult.failed("批量上传解析失败");
        }
    }

    /**
     * 2. 获取当前求职者绑定的所有简历记录
     */
    @GetMapping("/list")
    public CommonResult<List<CandidateResume>> getResumeList() {
        try {
            Long userId = UserContext.getUserId();
            List<CandidateResume> list = candidateResumeService.getResumeList(userId);
            return CommonResult.success(list);
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取简历列表失败");
        }
    }

    /**
     * 3. 设置某份简历为默认简历
     */
    @PutMapping("/default/{resumeId}")
    public CommonResult<Void> setDefaultResume(@PathVariable Long resumeId) {
        try {
            Long userId = UserContext.getUserId();
            candidateResumeService.setDefaultResume(userId, resumeId);
            return CommonResult.success(null, "默认简历设置成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("设置默认简历失败");
        }
    }

    /**
     * 4. 删除个人简历记录（同步删除物理文件）
     */
    @DeleteMapping("/delete/{resumeId}")
    public CommonResult<Void> deleteResume(@PathVariable Long resumeId) {
        try {
            Long userId = UserContext.getUserId();
            candidateResumeService.deleteResume(userId, resumeId);
            return CommonResult.success(null, "简历删除成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("系统繁忙，删除简历失败");
        }
    }

    /**
     * 6. 批量物理删除简历
     */
    @DeleteMapping("/delete-batch")
    public CommonResult<Void> deleteResumes(@RequestBody List<Long> resumeIds) {
        try {
            Long userId = UserContext.getUserId();
            candidateResumeService.deleteResumes(userId, resumeIds);
            return CommonResult.success(null, "选中的简历均已成功物理擦除");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("批量擦除失败");
        }
    }

    /**
     * 7. 智能解析简历画像（测试阶段：直接返回硬编码 Mock 数据，避免请求 Python AI 消耗 Token）
     * 路由：GET /api/portal/candidate/resume/parse/{resumeId}
     */
    @GetMapping("/parse/{resumeId}")
    public CommonResult<Object> parseResume(@PathVariable Long resumeId) {
        try {
            Long userId = UserContext.getUserId();

            // 1. 安全校验：确保当前用户有该简历的权限
            CandidateResume resume = candidateResumeService.getById(resumeId);
            if (resume == null || !resume.getUserId().equals(userId)) {
                return CommonResult.validateFailed("简历不存在或无权操作");
            }

            // ==================== 🎯 硬编码 Mock 数据开始 ====================

            // 1. 构建最内层的 AI 解析画像数据体 (data.data)
            Map<String, Object> aiData = new HashMap<>();
            aiData.put("name", "黄胜");
            aiData.put("education", "大专，滁州职业技术学院，软件技术专业");
            aiData.put("expertise", Arrays.asList(
                    "Java", "Spring Boot", "MyBatis", "MyBatis-Plus",
                    "Vue2", "Vue3", "ElementUI", "Axios",
                    "UniApp", "ECharts", "DataV", "Redis", "MySQL"
            ));
            aiData.put("artifacts", Arrays.asList(
                    "乐行旅途小程序（已上线微信）",
                    "凌云智控物联网设备管理平台（含实时监控大屏，已部署）",
                    "个人项目展示全栈平台（个人网页版简历）",
                    "3项软件著作权"
            ));
            aiData.put("dmSql", "未在简历中提及");
            aiData.put("llmAi", "未在简历中提及");
            aiData.put("language", "未在简历中提及");

            // 2. 模拟 Python FastAPI 端返回的外层包裹结构 (data)
            Map<String, Object> pythonResponse = new HashMap<>();
            pythonResponse.put("code", 200);
            pythonResponse.put("success", true);
            pythonResponse.put("message", "AI 深度画像提取完毕");
            pythonResponse.put("data", aiData);

            // 3. 通过 CommonResult.success 包装后返回（对应最外层的 code、message、data）
            return CommonResult.success(pythonResponse);

            // ==================== 🎯 硬编码 Mock 数据结束 ====================

        } catch (Exception e) {
            return CommonResult.failed("AI 代理服务发生阻碍: " + e.getMessage());
        }
    }

//    /**
//     * 7. 智能解析简历画像（前端调用 Java，Java 代理请求 Python AI）
//     * 路由：GET /api/portal/candidate/resume/parse/{resumeId}
//     */
//    @GetMapping("/parse/{resumeId}")
//    public CommonResult<Object> parseResume(@PathVariable Long resumeId) {
//        try {
//            Long userId = UserContext.getUserId();
//
//            // 1. 获取数据库中的文件存储路径
//            CandidateResume resume = candidateResumeService.getById(resumeId);
//            if (resume == null || !resume.getUserId().equals(userId)) {
//                return CommonResult.validateFailed("简历不存在或无权操作");
//            }
//
//            // 2. 获取本地磁盘的物理文件
//            File file = new File(resume.getFilePath());
//            if (!file.exists()) {
//                return CommonResult.failed("本地磁盘未检测到对应的简历文件实体");
//            }
//
//            // 3. 构建 MultiValueMap，模拟浏览器表单向 Python FastAPI 发送二进制数据
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("file", new FileSystemResource(file));
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            // 4. 发送 POST 请求给本地 Python (kyc-ai-service)
//            String pythonUrl = "http://127.0.0.1:8002/api/ai/resume/parse";
//            ResponseEntity<Object> response = restTemplate.postForEntity(pythonUrl, requestEntity, Object.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                // 5. 顺畅返回 Python 大模型解析出来的 JSON 画像
//                return CommonResult.success(response.getBody());
//            } else {
//                return CommonResult.failed("AI 引擎调用异常");
//            }
//        } catch (Exception e) {
//            return CommonResult.failed("AI 代理服务发生阻碍: " + e.getMessage());
//        }
//    }
}
