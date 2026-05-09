package com.kyc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyc.common.JwtUtil;
import com.kyc.entity.Candidate;
import com.kyc.mapper.CandidateMapper;
import com.kyc.model.dto.portal.CandidateRegisterDTO;
import com.kyc.model.dto.portal.LoginDTO;
import com.kyc.model.vo.portal.LoginResultVO;
import com.kyc.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * @program: kyc-api
 * @description: 登录注册核心业务逻辑实现
 * @author: 黄胜
 * @create: 2026-05-08 14:12
 **/


/**
 * 个人用户服务类实现类
 */
@Service
public class CandidateServiceImpl extends ServiceImpl<CandidateMapper, Candidate> implements CandidateService {

    @Autowired
    private JwtUtil jwtUtil;

    // MD5 混淆盐值，用于将明文密码加密，增强数据库安全性
    private static final String SALT = "kyc_candidate_secure_salt_2026";

    @Override
    public void register(CandidateRegisterDTO registerDTO) {
        String phone = registerDTO.getPhone();
        String email = registerDTO.getEmail();
        String password = registerDTO.getPassword();

        // 1. 基础非空盘查
        if (!StringUtils.hasText(password)) {
            throw new RuntimeException("密码不能为空");
        }
        if (!StringUtils.hasText(phone) && !StringUtils.hasText(email)) {
            throw new RuntimeException("手机号或邮箱必须填写一项");
        }

        Candidate candidate = new Candidate();

        // 2. 注册途径二选一校验并注入
        if (StringUtils.hasText(phone)) {
            // 手机号注册路径
            Long count = lambdaQuery().eq(Candidate::getPhone, phone).count();
            if (count > 0) {
                throw new RuntimeException("该手机号已被注册");
            }
            candidate.setPhone(phone);
            // 兼容底层组件可能需要非空 username 的设计，默认将 username 置为手机号
            candidate.setUsername(phone);
        } else {
            // 邮箱注册路径
            Long count = lambdaQuery().eq(Candidate::getEmail, email).count();
            if (count > 0) {
                throw new RuntimeException("该邮箱已被注册");
            }
            candidate.setEmail(email);
            candidate.setUsername(email);
        }

        // 3. 填充基础默认字段（真实姓名、头像等留到后续完善）
        candidate.setStatus(0); // 默认账号状态：0-正常
        candidate.setRole("candidate"); // 默认角色：个人用户
        candidate.setJobStatus("active"); // 默认求职状态：积极求职

        // 4. 进行安全级别极高的 MD5 盐值加密
        String md5Password = DigestUtils.md5DigestAsHex((password + SALT).getBytes());
        candidate.setPassword(md5Password);

        // 5. 调用 MyBatis-Plus 内置的 save 写入达梦数据库
        this.save(candidate);
    }

    @Override
    public LoginResultVO login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        if (!StringUtils.hasText(account) || !StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("账号和密码不能为空");
        }

        // 1. 穿透至达梦数据库，同时匹配手机号或邮箱（等值 eq 查询，并在底层用 OR 拼接）
        Candidate candidate = lambdaQuery()
                .eq(Candidate::getPhone, account)
                .or()
                .eq(Candidate::getEmail, account)
                .one();

        if (candidate == null) {
            throw new RuntimeException("账号或密码错误");
        }

        // 2. 检查账号状态（若 status 为 1，则代表被管理员冻结）
        if (candidate.getStatus() == 1) {
            throw new RuntimeException("该账号已被冻结，请联系管理员");
        }

        // 3. 将输入的明文密码加上盐值进行 MD5 转换，与达梦里的密文进行比对
        String md5Password = DigestUtils.md5DigestAsHex((loginDTO.getPassword() + SALT).getBytes());
        if (!candidate.getPassword().equals(md5Password)) {
            throw new RuntimeException("账号或密码错误");
        }

        // 4. 校验全部通过，使用 JWT 签发安全 Token（令牌）
        String token = jwtUtil.createToken(candidate.getId().toString(), "candidate");

        // 5. 封装前端需要呈现的用户极简卡片数据
        LoginResultVO result = new LoginResultVO();
        result.setToken(token);

        return result;
    }

    @Override
    public Candidate getProfile(Long id) {
        if (id == null) {
            throw new RuntimeException("用户凭证无效");
        }
        // 1. 查询达梦数据库，确保未被软删除
        Candidate candidate = getById(id);
        if (candidate == null || candidate.getDeleted() == 1) {
            throw new RuntimeException("用户不存在或已被注销");
        }
        // 2. 将敏感密码置空，防止通过查询接口泄露至前端
        candidate.setPassword(null);
        return candidate;
    }

    @Override
    public void updateProfile(Candidate candidate) {
        if (candidate == null || candidate.getId() == null) {
            throw new RuntimeException("更新请求无效");
        }

        // 1. 验证目标用户是否存在
        Candidate exist = getById(candidate.getId());
        if (exist == null) {
            throw new RuntimeException("目标更新用户不存在");
        }

        // 2. 手机号防重校验（如果修改了手机号）
        if (StringUtils.hasText(candidate.getPhone()) && !candidate.getPhone().equals(exist.getPhone())) {
            long count = lambdaQuery().eq(Candidate::getPhone, candidate.getPhone()).count();
            if (count > 0) {
                throw new RuntimeException("该手机号码已被其他账户绑定");
            }
        }

        // 3. 邮箱防重校验（如果修改了邮箱）
        if (StringUtils.hasText(candidate.getEmail()) && !candidate.getEmail().equals(exist.getEmail())) {
            long count = lambdaQuery().eq(Candidate::getEmail, candidate.getEmail()).count();
            if (count > 0) {
                throw new RuntimeException("该邮箱地址已被其他账户绑定");
            }
        }

        // 4. 只允许用户修改基本信息（昵称、头像、邮箱、手机、求职状态）
        Candidate updateEntity = new Candidate();
        updateEntity.setId(candidate.getId());
        updateEntity.setNickname(candidate.getNickname());
        updateEntity.setUsername(candidate.getUsername());
        updateEntity.setAvatar(candidate.getAvatar());
        updateEntity.setEmail(candidate.getEmail());
        updateEntity.setPhone(candidate.getPhone());
        updateEntity.setJobStatus(candidate.getJobStatus());

        boolean success = updateById(updateEntity);
        if (!success) {
            throw new RuntimeException("达梦数据库写入失败，请检查网络连接");
        }
    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        if (id == null || !StringUtils.hasText(currentPassword) || !StringUtils.hasText(newPassword)) {
            throw new RuntimeException("参数不完整，无法重置密码");
        }

        // 1. 获取库中最新数据
        Candidate exist = getById(id);
        if (exist == null) {
            throw new RuntimeException("账号不存在");
        }

        // 2. 校验旧密码：将输入的旧密码加盐 MD5 加密，比对数据库密文
        String md5Current = DigestUtils.md5DigestAsHex((currentPassword + SALT).getBytes());
        if (!exist.getPassword().equals(md5Current)) {
            throw new RuntimeException("原密码验证失败，请输入正确的旧密码");
        }

        // 3. 校验新旧密码不能相同
        String md5New = DigestUtils.md5DigestAsHex((newPassword + SALT).getBytes());
        if (exist.getPassword().equals(md5New)) {
            throw new RuntimeException("新密码不能与原密码相同");
        }

        // 4. 更新密码实体
        Candidate updateEntity = new Candidate();
        updateEntity.setId(id);
        updateEntity.setPassword(md5New);

        boolean success = updateById(updateEntity);
        if (!success) {
            throw new RuntimeException("密码更新失败，请重试");
        }
    }
}