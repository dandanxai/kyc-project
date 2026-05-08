package com.kyc.service;

/**
 * @program: kyc-api
 * @description: 登录注册业务接口
 * @author: 黄胜
 * @create: 2026-05-08 14:14
 **/


import com.baomidou.mybatisplus.extension.service.IService;
import com.kyc.entity.Candidate;
import com.kyc.model.dto.portal.CandidateRegisterDTO;
import com.kyc.model.dto.portal.LoginDTO;
import com.kyc.model.vo.portal.LoginResultVO;

/**
 * 个人用户服务类接口
 */
public interface CandidateService extends IService<Candidate> {

    /**
     * 个人用户注册（支持 手机号/邮箱 二选一）
     * @param registerDTO 注册表单数据
     */
    void register(CandidateRegisterDTO registerDTO);

    /**
     * 个人用户登录（支持 手机号/邮箱 + 密码）
     * @param loginDTO 登录表单数据
     * @return 登录成功后的用户信息及 Token 凭证
     */
    LoginResultVO login(LoginDTO loginDTO);

    /**
     * 获取个人详细信息 (用于回显基本资料)
     * @param id 用户主键 ID
     * @return 个人实体信息
     */
    Candidate getProfile(Long id);

    /**
     * 更新个人基本资料 (安全写入达梦数据库)
     * @param candidate 包含更新字段的实体类
     */
    void updateProfile(Candidate candidate);

    /**
     * 修改登录密码 (包含旧密码安全比对校验)
     * @param id 用户主键 ID
     * @param currentPassword 原明文密码
     * @param newPassword 新明文密码
     */
    void updatePassword(Long id, String currentPassword, String newPassword);
}