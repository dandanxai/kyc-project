package com.kyc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyc.entity.EnterpriseUser;
import com.kyc.model.dto.enterprise.EnterpriseRegisterDTO;
import com.kyc.model.dto.enterprise.EnterpriseLoginDTO;
import com.kyc.model.vo.enterprise.EnterpriseLoginResultVO;

public interface EnterpriseUserService extends IService<EnterpriseUser> {

    /**
     * 企业账号注册
     */
    void register(EnterpriseRegisterDTO registerDTO);

    /**
     * 企业账号登录
     */
    EnterpriseLoginResultVO login(EnterpriseLoginDTO loginDTO);

    /**
     * 获取当前登录企业用户的详细资料
     */
    EnterpriseUser getProfile(Long id);

    /**
     * 更新企业资料（从 EnterpriseContext 自动提取当前企业 ID）
     */
    void updateProfile(EnterpriseUser enterpriseUser);

    /**
     * 变更企业密码（从 EnterpriseContext 自动提取当前企业 ID）
     */
    void updatePassword(String currentPassword, String newPassword);
}