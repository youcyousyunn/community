package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiVacationTaskDao;
import com.ycs.community.activiti.domain.dto.ActivitiVacationTaskRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageResponseDto;
import com.ycs.community.activiti.domain.po.FlowMain;
import com.ycs.community.activiti.domain.po.VacationTaskPo;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.activiti.service.ActivitiVacationTaskService;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiVacationTaskServiceImpl implements ActivitiVacationTaskService {
    @Autowired
    private ActivitiVacationTaskDao activitiVacationTaskDao;
    @Autowired
    private ActivitiFlowService activitiInfoService;
    @Autowired
    private TaskService taskService;


    @Override
    public boolean delVacationTaskById(Long id) {
        // 更新请假审批单状态
        this.updVacationTaskStateById(id, Constants.OBSOLETE_STATE);
        return true;
    }

    @Override
    public QryActivitiVacationTaskPageResponseDto qryMyVacationTaskPage(QryActivitiVacationTaskPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        long userId = SecurityUtil.getUserId();
        paramMap.put("userId", userId);
        if (!StringUtils.isEmpty(request.getTitle())) {
            paramMap.put("title", request.getTitle());
        }
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = activitiVacationTaskDao.qryMyVacationTaskCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<VacationTaskPo> data = activitiVacationTaskDao.qryMyVacationTaskPage(paramMap);
        // 组装分页信息
        QryActivitiVacationTaskPageResponseDto response = new QryActivitiVacationTaskPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addVacationTask(ActivitiVacationTaskRequestDto request) {
        long userId = SecurityUtil.getUserId();
        VacationTaskPo vacationTaskPo = new VacationTaskPo();
        vacationTaskPo.setFlowDefId(request.getFlowDefId());
        vacationTaskPo.setUserId(userId);
        vacationTaskPo.setType(request.getType());
        vacationTaskPo.setTitle(request.getTitle());
        vacationTaskPo.setContext(request.getContext());
        vacationTaskPo.setStartTime(request.getStartTime().getTime());
        vacationTaskPo.setEndTime(request.getEndTime().getTime());
        vacationTaskPo.setState(Constants.SUBMITTED_STATE);
        vacationTaskPo.setCreTm(new Date().getTime());
        if(activitiVacationTaskDao.addVacationTask(vacationTaskPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_VACATION_TASK_FAIL, "添加请假申请失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updVacationTask(ActivitiVacationTaskRequestDto request) {
        VacationTaskPo vacationTaskPo = new VacationTaskPo();
        vacationTaskPo.setFlowDefId(request.getFlowDefId());
        vacationTaskPo.setType(request.getType());
        vacationTaskPo.setTitle(request.getTitle());
        vacationTaskPo.setContext(request.getContext());
        vacationTaskPo.setStartTime(request.getStartTime().getTime());
        vacationTaskPo.setEndTime(request.getEndTime().getTime());
        vacationTaskPo.setState(request.getState());
        vacationTaskPo.setUpdTm(new Date().getTime());
        if(activitiVacationTaskDao.updVacationTask(vacationTaskPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_VACATION_TASK_FAIL, "更新请假申请失败");
        }
        return true;
    }

    @Override
    @Transactional (rollbackFor = {CustomizeBusinessException.class})
    public boolean submitVacationTask(ActivitiVacationTaskRequestDto request) {
        // 设置流程审批人
        Map<String, Object> variables = new HashMap<>();
        String flowId = "";
        // 匹配流程之前查询是否已经匹配过
        FlowMain flowMain = activitiInfoService.qryFlowMainByTaskId(request.getId());
        if(StringUtils.isEmpty(flowMain)) {
            variables.put("applyuser",request.getAssignee());
            flowId = activitiInfoService.resolve(request.getId(), request.getFlowDefId(), variables);
        } else {
            flowId = String.valueOf(flowMain.getFlowId());
        }
        if(StringUtils.isEmpty(flowId)) {
            return false;
        }

        // 提交工作流
        Task task =  activitiInfoService.qryTaskByInstId(flowId);
        if(StringUtils.isEmpty(task)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.FLOW_NOT_EXIST, "流程已不存在");
        }
        variables.put("subState","success");
        taskService.complete(task.getId(), variables);
        // 更新审批单状态
        this.updVacationTaskStateById(request.getId(), Constants.REVIEW_STATE);
        return true;
    }

    private boolean updVacationTaskStateById(Long id, int state) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("state", state);
        if(activitiVacationTaskDao.updVacationTaskStateById(paramMap) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_VACATION_TASK_STATE_FAIL, "更新请假状态失败");
        }
        return true;
    }
}
