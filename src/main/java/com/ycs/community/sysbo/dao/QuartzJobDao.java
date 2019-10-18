package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuartzJobDao {
    int addJob(QuartzJobPo quartzJobPo);
}
