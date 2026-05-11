package com.kyc.controller.enterprise;

import com.kyc.common.api.CommonResult;
import com.kyc.common.context.EnterpriseContext;
import com.kyc.entity.EnterpriseUser;
import com.kyc.model.dto.enterprise.EnterpriseRegisterDTO;
import com.kyc.model.dto.enterprise.EnterpriseLoginDTO;
import com.kyc.model.dto.enterprise.EnterpriseUpdateDTO;
import com.kyc.model.dto.enterprise.EnterprisePasswordUpdateDTO;
import com.kyc.model.vo.enterprise.EnterpriseLoginResultVO;
import com.kyc.service.EnterpriseUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kyc-api
 * @description: 企业端用户基本信息及安全维护控制器
 * @author: 黄胜
 * @create: 2026-05-11 16:00
 **/
@RestController
@RequestMapping("/api/enterprise/user")
@CrossOrigin // 解决前端 Local 调试端口跨域问题
public class EnterpriseUserController {

    @Autowired
    private EnterpriseUserService enterpriseUserService;

    /**
     * 1. 企业账号自主注册
     */
    @PostMapping("/register")
    public CommonResult<Void> register(@RequestBody EnterpriseRegisterDTO registerDTO) {
        try {
            enterpriseUserService.register(registerDTO);
            return CommonResult.success(null, "企业注册成功，请等待管理员审核");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("系统繁忙，企业注册失败");
        }
    }

    /**
     * 2. 企业账号登录
     */
    @PostMapping("/login")
    public CommonResult<EnterpriseLoginResultVO> login(@RequestBody EnterpriseLoginDTO loginDTO) {
        try {
            EnterpriseLoginResultVO loginResult = enterpriseUserService.login(loginDTO);
            return CommonResult.success(loginResult, "登录成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("系统繁忙，登录失败");
        }
    }

    /**
     * 3. 查询当前登录企业用户的档案详情
     */
    @GetMapping("/profile")
    public CommonResult<EnterpriseUser> getProfile() {
        try {
            Long enterpriseId = EnterpriseContext.getEnterpriseId();
            if (enterpriseId == null) {
                return CommonResult.unauthorized("您还未登录或凭证失效");
            }
            EnterpriseUser profile = enterpriseUserService.getProfile(enterpriseId);
            return CommonResult.success(profile);
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("获取资料失败");
        }
    }

    /**
     * 4. 更新当前企业核心业务资料
     */
    @PutMapping("/profile/update")
    public CommonResult<EnterpriseUser> updateProfile(@RequestBody EnterpriseUpdateDTO updateDTO) {
        try {
            Long enterpriseId = EnterpriseContext.getEnterpriseId();
            if (enterpriseId == null) {
                return CommonResult.unauthorized("未检测到有效登录状态");
            }

            EnterpriseUser updateEntity = new EnterpriseUser();
            BeanUtils.copyProperties(updateDTO, updateEntity);

            // 🎯 强制覆盖，将上下文解析出来的安全 ID 注入到实体中，防前端篡改
            updateEntity.setId(enterpriseId);

            // 🎯 调用简化后的 Service 接口
            enterpriseUserService.updateProfile(updateEntity);

            // 获取最新更新后的结果返回前端
            EnterpriseUser updated = enterpriseUserService.getProfile(enterpriseId);
            return CommonResult.success(updated, "企业档案更新成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("更新企业档案失败");
        }
    }

    /**
     * 5. 变更企业账户登录密码
     */
    @PutMapping("/password/update")
    public CommonResult<Void> updatePassword(@RequestBody EnterprisePasswordUpdateDTO passwordDTO) {
        try {
            Long enterpriseId = EnterpriseContext.getEnterpriseId();
            if (enterpriseId == null) {
                return CommonResult.unauthorized("未检测到有效登录状态");
            }

            // 🎯 调用简化后的 Service 接口，不再需要显式传递 ID 参数
            enterpriseUserService.updatePassword(
                    passwordDTO.getCurrentPassword(),
                    passwordDTO.getNewPassword()
            );
            return CommonResult.success(null, "企业登录密码修改成功");
        } catch (RuntimeException e) {
            return CommonResult.validateFailed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("密码修改异常");
        }
    }
}