package com.kyc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyc.common.JwtUtil;
import com.kyc.common.context.EnterpriseContext;
import com.kyc.entity.EnterpriseUser;
import com.kyc.mapper.EnterpriseUserMapper;
import com.kyc.model.dto.enterprise.EnterpriseRegisterDTO;
import com.kyc.model.dto.enterprise.EnterpriseLoginDTO;
import com.kyc.model.vo.enterprise.EnterpriseLoginResultVO;
import com.kyc.service.EnterpriseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * 企业用户服务实现类
 */
@Service
public class EnterpriseUserServiceImpl extends ServiceImpl<EnterpriseUserMapper, EnterpriseUser> implements EnterpriseUserService {

    @Autowired
    private JwtUtil jwtUtil;

    // 🎯 独立的企业级 MD5 混淆盐值，保障后台安全
    private static final String SALT = "kyc_enterprise_secure_salt_2026";

    @Override
    public void register(EnterpriseRegisterDTO registerDTO) {
        if (registerDTO == null || !StringUtils.hasText(registerDTO.getUsername()) || !StringUtils.hasText(registerDTO.getPassword())) {
            throw new RuntimeException("注册核心数据不能为空");
        }

        // 1. 唯一性校验：校验账号是否已注册
        Long countUsername = count(new LambdaQueryWrapper<EnterpriseUser>()
                .eq(EnterpriseUser::getUsername, registerDTO.getUsername()));
        if (countUsername > 0) {
            throw new RuntimeException("该登录账号已被注册，请更换账号名称");
        }

        // 2. 唯一性校验：校验企业社会信用代码是否被抢注
        if (StringUtils.hasText(registerDTO.getEnterpriseCode())) {
            Long countCode = count(new LambdaQueryWrapper<EnterpriseUser>()
                    .eq(EnterpriseUser::getEnterpriseCode, registerDTO.getEnterpriseCode()));
            if (countCode > 0) {
                throw new RuntimeException("该统一社会信用代码对应的企业已被注册");
            }
        }

        // 3. 构建实体，加盐 MD5 密码处理后持久化
        EnterpriseUser enterprise = new EnterpriseUser();
        enterprise.setUsername(registerDTO.getUsername());

        // 🔒 使用 MD5 + SALT 进行密码单向混淆
        String md5Password = DigestUtils.md5DigestAsHex((registerDTO.getPassword() + SALT).getBytes());
        enterprise.setPassword(md5Password);

        enterprise.setEnterpriseName(registerDTO.getEnterpriseName());
        enterprise.setLegalPerson(registerDTO.getLegalPerson());
        enterprise.setEnterpriseCode(registerDTO.getEnterpriseCode());
        enterprise.setContactName(registerDTO.getContactName());
        enterprise.setContactPhone(registerDTO.getContactPhone());
        enterprise.setEmail(registerDTO.getEmail());
        enterprise.setStatus(0); // 默认待审核，需后台人工介入或测试环境手动修改状态为1才可登录

        boolean success = save(enterprise);
        if (!success) {
            throw new RuntimeException("注册失败，达梦数据库写入异常");
        }
    }

    @Override
    public EnterpriseLoginResultVO login(EnterpriseLoginDTO loginDTO) {
        if (loginDTO == null || !StringUtils.hasText(loginDTO.getUsername()) || !StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("用户名或密码未输入");
        }

        // 1. 支持多维度检索企业（账号、社会信用代码或绑定电话）
        EnterpriseUser enterprise = getOne(new LambdaQueryWrapper<EnterpriseUser>()
                .eq(EnterpriseUser::getUsername, loginDTO.getUsername())
                .or()
                .eq(EnterpriseUser::getEnterpriseCode, loginDTO.getUsername())
                .or()
                .eq(EnterpriseUser::getContactPhone, loginDTO.getUsername()));

        if (enterprise == null) {
            throw new RuntimeException("企业账号不存在，请确认后重新输入");
        }

        // 2. 比对加密密码
        String md5Input = DigestUtils.md5DigestAsHex((loginDTO.getPassword() + SALT).getBytes());
        if (!enterprise.getPassword().equals(md5Input)) {
            throw new RuntimeException("密码输入错误");
        }

        // 3. 校验账号状态
        if (enterprise.getStatus() == 0) {
            throw new RuntimeException("企业账号还在审核中，请耐心等待管理员审核");
        } else if (enterprise.getStatus() == 2) {
            throw new RuntimeException("企业资质审核未通过，请联系管理员");
        } else if (enterprise.getStatus() == 3) {
            throw new RuntimeException("该企业账号已被安全管制禁用");
        }

        // 4. 生成 Token (调用已在 JwtUtil 中兼容处理好的 generateToken)
        String token = jwtUtil.createToken(enterprise.getId().toString(), "enterprise");
        EnterpriseLoginResultVO vo = new EnterpriseLoginResultVO();
        vo.setToken(token);
        return vo;
    }

    @Override
    public EnterpriseUser getProfile(Long id) {
        EnterpriseUser enterprise = getById(id);
        if (enterprise == null) {
            throw new RuntimeException("未找到相关企业数据");
        }
        // 屏蔽敏感密码哈希
        enterprise.setPassword(null);
        return enterprise;
    }

    @Override
    public void updateProfile(EnterpriseUser info) {
        // 🎯 优化：从 EnterpriseContext 隐式获取 ID，不传参、防越权
        Long enterpriseId = EnterpriseContext.getEnterpriseId();
        if (enterpriseId == null) {
            throw new RuntimeException("未检测到有效登录状态");
        }

        EnterpriseUser exist = getById(enterpriseId);
        if (exist == null) {
            throw new RuntimeException("修改失败，该企业账号已不存在");
        }

        // 构建安全更新数据实体（防止前端传参破坏不该修改的字段，如密码、审核状态等）
        EnterpriseUser updateEntity = new EnterpriseUser();
        updateEntity.setId(enterpriseId);
        updateEntity.setEnterpriseName(info.getEnterpriseName());
        updateEntity.setLegalPerson(info.getLegalPerson());
        updateEntity.setIndustry(info.getIndustry());
        updateEntity.setScale(info.getScale());
        updateEntity.setNature(info.getNature());
        updateEntity.setLogo(info.getLogo());
        updateEntity.setLicenseUrl(info.getLicenseUrl());
        updateEntity.setContactName(info.getContactName());
        updateEntity.setContactPhone(info.getContactPhone());
        updateEntity.setEmail(info.getEmail());
        updateEntity.setIntroduction(info.getIntroduction());

        boolean success = updateById(updateEntity);
        if (!success) {
            throw new RuntimeException("修改失败，达梦数据库写入异常");
        }
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword) {
        // 🎯 优化：从 EnterpriseContext 隐式获取 ID
        Long enterpriseId = EnterpriseContext.getEnterpriseId();
        if (enterpriseId == null) {
            throw new RuntimeException("未检测到有效登录状态");
        }

        if (!StringUtils.hasText(currentPassword) || !StringUtils.hasText(newPassword)) {
            throw new RuntimeException("密码修改参数不完整");
        }

        EnterpriseUser exist = getById(enterpriseId);
        if (exist == null) {
            throw new RuntimeException("企业账号不存在");
        }

        // 比对原密码
        String md5Current = DigestUtils.md5DigestAsHex((currentPassword + SALT).getBytes());
        if (!exist.getPassword().equals(md5Current)) {
            throw new RuntimeException("原密码校验失败");
        }

        // 校验新密码是否和原密码一致
        String md5New = DigestUtils.md5DigestAsHex((newPassword + SALT).getBytes());
        if (exist.getPassword().equals(md5New)) {
            throw new RuntimeException("新密码不能与原密码相同");
        }

        EnterpriseUser updateEntity = new EnterpriseUser();
        updateEntity.setId(enterpriseId);
        updateEntity.setPassword(md5New);

        boolean success = updateById(updateEntity);
        if (!success) {
            throw new RuntimeException("达梦数据库密码更新异常");
        }
    }
}