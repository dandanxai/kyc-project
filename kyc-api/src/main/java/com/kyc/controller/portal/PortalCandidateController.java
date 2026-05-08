package com.kyc.controller.portal;

import com.kyc.common.api.CommonResult;
import com.kyc.entity.Candidate;
import com.kyc.model.dto.portal.CandidateUpdateDTO;
import com.kyc.model.dto.portal.PasswordUpdateDTO;
import com.kyc.service.CandidateService;
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
    @GetMapping("/profile/{id}")
    public CommonResult<Candidate> getProfile(@PathVariable Long id) {
        try {
            Candidate candidate = candidateService.getProfile(id);
            return CommonResult.success(candidate);
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取资料失败，系统繁忙");
        }
    }

    /**
     * 2. 更新个人基本资料 (对应前端 updateCandidateProfile)
     * 路由：PUT /api/portal/candidate/profile/update
     */
    @PutMapping("/profile/update")
    public CommonResult<Candidate> updateProfile(@RequestBody CandidateUpdateDTO updateDTO) {
        try {
            Candidate candidate = new Candidate();
            BeanUtils.copyProperties(updateDTO, candidate);

            candidateService.updateProfile(candidate);
            // 重新获取更新后的完整实体返给前端
            Candidate updated = candidateService.getProfile(updateDTO.getId());
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
     */
    @PutMapping("/password/update")
    public CommonResult<Void> updatePassword(@RequestBody PasswordUpdateDTO passwordDTO) {
        try {
            candidateService.updatePassword(
                    passwordDTO.getId(),
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