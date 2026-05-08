package com.kyc.mapper;

/**
 * @program: kyc-api
 * @description:
 * @author: 黄胜
 * @create: 2026-05-08 14:16
 **/


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyc.entity.Candidate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 个人用户 Mapper 接口
 * 继承 BaseMapper 后，自动拥有了对 kyc_candidate 表的增删改查（CRUD）能力，无需编写任何 SQL/XML
 */
@Mapper
public interface CandidateMapper extends BaseMapper<Candidate> {
    // 如果后续有极其复杂的“多表联查”或者“大数据量统计”，再在这里写自定义的 SQL 方法
}