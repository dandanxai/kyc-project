package com.kyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyc.entity.CandidateResume;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: kyc-api
 * @description: 候选人简历 Mapper 接口
 * @author: 黄胜
 * @create: 2026-05-10 11:22
 **/
@Mapper
public interface CandidateResumeMapper extends BaseMapper<CandidateResume> {
    // 继承 BaseMapper，直接拥有对 kyc_candidate_resume 表的 CRUD 能力
}
