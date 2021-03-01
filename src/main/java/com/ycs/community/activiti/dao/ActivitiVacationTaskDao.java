package com.ycs.community.activiti.dao;

import com.ycs.community.activiti.domain.po.VacationTaskPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ActivitiVacationTaskDao {
    int addVacationTask(VacationTaskPo vacationTaskPo);
    int qryMyVacationTaskCount(Map<String, Object> paramMap);
    List<VacationTaskPo> qryMyVacationTaskPage(Map<String, Object> paramMap);
    int qryAllVacationTaskCount(Map<String, Object> paramMap);
    List<VacationTaskPo> qryAllVacationTaskPage(Map<String, Object> paramMap);
    int updVacationTask(VacationTaskPo vacationTaskPo);
    int updVacationTaskAssigneeById(Map<String, Object> paramMap);
    int updVacationTaskStateById(Map<String, Object> paramMap);
}
