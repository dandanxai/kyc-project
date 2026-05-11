package com.kyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyc.entity.EnterpriseUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业用户 Mapper 接口
 */
@Mapper
public interface EnterpriseUserMapper extends BaseMapper<EnterpriseUser> {
}