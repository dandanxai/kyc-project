package com.kyc.controller.portal;

import com.kyc.common.api.CommonResult;
import com.kyc.model.dto.portal.CandidateRegisterDTO;
import com.kyc.model.dto.portal.LoginDTO;
import com.kyc.model.vo.portal.LoginResultVO;
import com.kyc.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kyc-api
 * @description: 个人用户登录注册控制器
 * @author: 黄胜
 * @create: 2026-05-08 14:30
 **/
@RestController
@RequestMapping("/api/portal/auth")
@CrossOrigin // 🎯 允许跨域（方便本地前端 5173 穿透访问 8080）
public class PortalAuthController {

    @Autowired
    private CandidateService candidateService;

    /**
     * 1. 个人用户注册接口（手机号或邮箱二选一）
     * 路由：POST /api/portal/auth/register
     */
    @PostMapping("/register")
    public CommonResult<Void> register(@RequestBody CandidateRegisterDTO registerDTO) {
        try {
            candidateService.register(registerDTO);
            return CommonResult.success(null, "注册成功");
        } catch (RuntimeException e) {
            // 捕获 Service 层抛出的业务异常（如：该手机号已被注册），并友好返回
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            // 捕获系统未预料的严重异常
            return CommonResult.failed("系统繁忙，请稍后再试");
        }
    }

    /**
     * 2. 个人用户登录接口（支持手机号或邮箱 + 密码）
     * 路由：POST /api/portal/auth/login
     */
    @PostMapping("/login")
    public CommonResult<LoginResultVO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResultVO loginResult = candidateService.login(loginDTO);
            return CommonResult.success(loginResult, "登录成功");
        } catch (RuntimeException e) {
            // 捕获业务异常（如：账号或密码错误、账号已被冻结）
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("登录服务出现异常，请联系系统管理员");
        }
    }
}