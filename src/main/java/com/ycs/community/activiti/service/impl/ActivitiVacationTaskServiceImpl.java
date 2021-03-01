package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiVacationTaskDao;
import com.ycs.community.activiti.domain.dto.ActivitiVacationTaskRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageResponseDto;
import com.ycs.community.activiti.domain.po.BaseTaskPo;
import com.ycs.community.activiti.domain.po.FlowMain;
import com.ycs.community.activiti.domain.po.ProcessLog;
import com.ycs.community.activiti.domain.po.VacationTaskPo;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.activiti.service.ActivitiProcessLogService;
import com.ycs.community.activiti.service.ActivitiTaskService;
import com.ycs.community.activiti.service.ActivitiVacationTaskService;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
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
    private UserService userService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivitiTaskService activitiTaskService;


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
        paramMap.put("applierId", userId);
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
        // 添加审批人
        data.forEach(vacationTaskPo -> {
            UserPo userPo = userService.qryUserById(vacationTaskPo.getAssigneeId());
            vacationTaskPo.setAssigneeName(userPo.getName());
        });
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
    public QryActivitiVacationTaskPageResponseDto queryAllVacationTask(QryActivitiVacationTaskPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
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
        int totalCount = activitiVacationTaskDao.qryAllVacationTaskCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<VacationTaskPo> data = activitiVacationTaskDao.qryAllVacationTaskPage(paramMap);
        // 组装分页信息
        QryActivitiVacationTaskPageResponseDto response = new QryActivitiVacationTaskPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    /**
     * 统一添加日志
     * @param processLog
     * @return
     */
    private boolean addProcessLog(ProcessLog processLog) {
        if(!activitiProcessLogService.addProcessLog(processLog)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_PROCESS_LOG_FAIL, "添加流程过程日志失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addVacationTask(ActivitiVacationTaskRequestDto request) {
        long userId = SecurityUtil.getUserId();
        VacationTaskPo vacationTaskPo = new VacationTaskPo();
        vacationTaskPo.setFlowDefId(request.getFlowDefId());
        vacationTaskPo.setApplierId(userId);
        vacationTaskPo.setAssigneeId(request.getAssigneeId());
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

        //记录日志
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(vacationTaskPo.getId());
        processLog.setTaskName("填写申请");
        processLog.setApproveStatus("apply");
        processLog.setOperUserId(userId);
        processLog.setOperValue(SecurityUtil.getUserName() + "填写了请假申请单");
        processLog.setRemark(vacationTaskPo.getContext());
        processLog.setCreTm(new Date().getTime());
        this.addProcessLog(processLog);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updVacationTask(ActivitiVacationTaskRequestDto request) {
        VacationTaskPo vacationTaskPo = new VacationTaskPo();
        vacationTaskPo.setId(request.getId());
        vacationTaskPo.setFlowDefId(request.getFlowDefId());
        vacationTaskPo.setAssigneeId(request.getAssigneeId());
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

        //记录日志
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(vacationTaskPo.getId());
        processLog.setTaskName("更新申请");
        processLog.setApproveStatus("update");
        processLog.setOperValue(SecurityUtil.getUserName() + "更新了请假申请单");
        processLog.setRemark(vacationTaskPo.getContext());
        processLog.setOperUserId(SecurityUtil.getUserId());
        processLog.setCreTm(new Date().getTime());
        this.addProcessLog(processLog);
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
            variables.put("applyuser",request.getAssigneeId());
            flowId = activitiInfoService.resolve(request.getId(), request.getFlowDefId(), variables);
        } else {
            flowId = String.valueOf(flowMain.getFlowId());
        }
        if(StringUtils.isEmpty(flowId)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.FLOW_NOT_EXIST, "流程已不存在");
        }

        // 提交工作流
        Task task =  activitiInfoService.qryTaskByInstId(flowId);
        if(StringUtils.isEmpty(task)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.FLOW_NOT_EXIST, "流程已不存在");
        }
        variables.put("subState","success");
        taskService.complete(task.getId(), variables);

        //更新act_ru_task表中任务代理人
        BaseTaskPo baseTaskPo = activitiTaskService.qryTaskByProcessId(request.getId());
        if(StringUtils.isEmpty(baseTaskPo)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.FLOW_TASK_NOT_EXIST, "流程任务已不存在");
        }
        taskService.setAssignee(baseTaskPo.getTaskId(), String.valueOf(request.getAssigneeId()));

        // 更新审批单状态
        this.updVacationTaskStateById(request.getId(), Constants.REVIEW_STATE);

        //记录日志
        UserPo userPo = userService.qryUserById(request.getAssigneeId());
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(request.getId());
        processLog.setTaskId(task.getId());
        processLog.setTaskName(task.getName());
        processLog.setTaskKey(task.getTaskDefinitionKey());
        processLog.setApproveStatus("submitApply");
        processLog.setOperUserId(SecurityUtil.getUserId());
        processLog.setOperValue(SecurityUtil.getUserName() + "提交请假申请单，待【" + userPo.getName() + "】审核");
        processLog.setRemark(request.getContext());
        processLog.setCreTm(new Date().getTime());
        this.addProcessLog(processLog);
        return true;
    }

    @Override
    @Transactional (rollbackFor = {CustomizeBusinessException.class})
    public boolean approveVacationTask(ActivitiVacationTaskRequestDto request) {
        Map<String, Object> variables = new HashMap<>();
        String spState = "";
        String spContext = "";
        if(Constants.APPROVAL_AGREE.equals(request.getApprovalType())) {
            spState = Constants.APPROVAL_AGREE;
            spContext = "审批通过";
        } else if(Constants.APPROVAL_REJECT.equals(request.getApprovalType())) {
            spState = Constants.APPROVAL_REJECT;
            spContext = "审核驳回";
        }

        //记录日志
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(request.getId());
        processLog.setTaskId(request.getTaskId());
        processLog.setTaskName(request.getTaskName());
        processLog.setApproveStatus(spState);
        processLog.setOperUserId(SecurityUtil.getUserId());
        processLog.setOperValue(SecurityUtil.getUserName() + spContext + "，审批意见：" + request.getRemark());
        processLog.setRemark(request.getRemark());
        processLog.setCreTm(new Date().getTime());
        this.addProcessLog(processLog);

        variables.put("spState", spState);
        taskService.complete(request.getTaskId(), variables);

        //更新act_ru_task表中任务代理人
        BaseTaskPo baseTaskPo = activitiTaskService.qryTaskByProcessId(request.getId());
        if(!StringUtils.isEmpty(baseTaskPo)) {
            taskService.setAssignee(baseTaskPo.getTaskId(), String.valueOf(request.getAssigneeId()));
            //更新请假单审批人
            this.updVacationTaskAssigneeById(request.getId(), request.getAssigneeId());
        }
        // 更新请假审批单状态
        if(Constants.APPROVAL_REJECT.equals(request.getApprovalType())) {
            this.updVacationTaskStateById(request.getId(), Constants.SUBMITTED_STATE);
        }

        return true;
    }

    @Override
    public boolean updVacationTaskAssigneeById(Long id, Long assigneeId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("assigneeId", assigneeId);
        if(activitiVacationTaskDao.updVacationTaskAssigneeById(paramMap) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_VACATION_TASK_ASSIGNEE_FAIL, "更新请假单审批人失败");
        }
        return true;
    }

    @Override
    public boolean updVacationTaskStateById(Long id, int state) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("state", state);
        if(activitiVacationTaskDao.updVacationTaskStateById(paramMap) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_VACATION_TASK_STATE_FAIL, "更新请假状态失败");
        }
        return true;
    }
}
