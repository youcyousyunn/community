package com.ycs.community.activiti.listener;

import com.ycs.community.activiti.domain.po.FlowMain;
import com.ycs.community.activiti.domain.po.ProcessLog;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.activiti.service.ActivitiProcessLogService;
import com.ycs.community.activiti.service.ActivitiVacationTaskService;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.context.SpringContextHolder;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Date;

/**
 * 审批完成监听器
 */
@Slf4j
public class FinishDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String flowInstId = execution.getProcessBusinessKey();
        ActivitiVacationTaskService activitiVacationTaskService = SpringContextHolder.getBean(ActivitiVacationTaskService.class);
        ActivitiFlowService flowInfoService = SpringContextHolder.getBean(ActivitiFlowService.class);
        ActivitiProcessLogService activitiProcessLogService = SpringContextHolder.getBean(ActivitiProcessLogService.class);
        FlowMain flowMain = flowInfoService.qryFlowMainById(Long.valueOf(flowInstId));
        activitiVacationTaskService.updVacationTaskStateById(flowMain.getProcessId(), Constants.COMPLETED_STATE);

        //记录日志
        Long userId = SecurityUtil.getUserId();
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(flowMain.getProcessId());
        processLog.setTaskName("审批完成");
        processLog.setApproveStatus("finish");
        processLog.setOperUserId(userId);
        processLog.setOperValue("审批结束");
        processLog.setCreTm(new Date().getTime());
        if(!activitiProcessLogService.addProcessLog(processLog)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_PROCESS_LOG_FAIL, "添加流程过程日志失败");
        }
    }
}
