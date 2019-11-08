package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.LogJnlPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface LogDao {
    int addLog(LogJnlPo logJnlPo);
    int qryLogCount(Map<String, Object> paramMap);
    List<LogJnlPo> qryLogPage(Map<String, Object> paramMap);
}
