package com.aurora.newMeans.userInfo.mapper;

import com.aurora.newMeans.userInfo.domain.YHUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface YHUserMapper extends BaseMapper<YHUser> {
}
