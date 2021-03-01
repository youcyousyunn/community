package com.ycs.community.activiti.dao;

import com.ycs.community.activiti.domain.po.BaseTaskPo;
import com.ycs.community.activiti.domain.po.TaskPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ActivitiTaskDao {
    int qryMyTaskCount(Map<String, Object> paramMap);
    List<TaskPo> qryMyTaskPage(Map<String, Object> paramMap);
    BaseTaskPo qryTaskByProcessId(Long processId);
}
