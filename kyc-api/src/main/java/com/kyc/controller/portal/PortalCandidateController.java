package com.kyc.controller.portal;

import com.kyc.common.api.CommonResult;
import com.kyc.common.context.UserContext;
import com.kyc.entity.Candidate;
import com.kyc.model.dto.portal.CandidateUpdateDTO;
import com.kyc.model.dto.portal.PasswordUpdateDTO;
import com.kyc.service.CandidateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kyc-api
 * @description: 个人求职者信息维护控制器
 * @author: 黄胜
 * @create: 2026-05-08 16:00
 **/
@RestController
@RequestMapping("/api/portal/candidate")
@CrossOrigin // 支持本地 5173 前端跨域请求
public class PortalCandidateController {

    @Autowired
    private CandidateService candidateService;

    /**
     * 1. 查询当前求职者详细资料
     * 路由：GET /api/portal/candidate/profile/{id}
     */
    @GetMapping("/profile")
    public CommonResult<Candidate> getProfile() {
        try {
            Long userId = UserContext.getUserId();
            Candidate candidate = candidateService.getProfile(userId);
            return CommonResult.success(candidate);
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取资料失败，系统繁忙");
        }
    }

    /**
     * 2. 更新个人基本资料 (免传任何 ID)
     * 路由：PUT /api/portal/candidate/profile/update
     */
    @PutMapping("/profile/update")
    public CommonResult<Candidate> updateProfile(@RequestBody CandidateUpdateDTO updateDTO) {
        try {
            Long currentUserId = UserContext.getUserId();

            Candidate candidate = new Candidate();
            BeanUtils.copyProperties(updateDTO, candidate);
            candidate.setId(currentUserId);

            candidateService.updateProfile(candidate);
            Candidate updated = candidateService.getProfile(currentUserId);
            return CommonResult.success(updated, "资料更新成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("系统繁忙，修改资料失败");
        }
    }

    /**
     * 3. 修改账户登录密码 (对应前端 updateCandidatePassword)
     * 路由：PUT /api/portal/candidate/password/update
     * 🎯 安全防线：拒绝相信前端传入的 id，强制使用拦截器解析出的真实 currentUserId
     */
    @PutMapping("/password/update")
    public CommonResult<Void> updatePassword(
            @RequestBody PasswordUpdateDTO passwordDTO,
            HttpServletRequest request
    ) {
        try {
            // 从拦截器已经存入的 Attribute 中薅出当前登录人的真实 ID
            String currentUserIdStr = (String) request.getAttribute("currentUserId");
            if (currentUserIdStr == null) {
                return CommonResult.unauthorized("未检测到有效登录状态");
            }
            Long currentUserId = Long.parseLong(currentUserIdStr);

            // 🎯 强制使用当前用户的真实安全 ID，直接无视并丢弃 passwordDTO 里的 id
            candidateService.updatePassword(
                    currentUserId,
                    passwordDTO.getCurrentPassword(),
                    passwordDTO.getNewPassword()
            );
            return CommonResult.success(null, "密码修改成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("系统繁忙，密码修改失败");
        }
    }
}