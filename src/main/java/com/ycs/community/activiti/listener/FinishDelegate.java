package com.ycs.community.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 审批完成监听器类
 */
@Slf4j
public class FinishDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String flowInstId = execution.getProcessBusinessKey();
//        IVacationOrderService vacationOrderService = SpringUtils.getBean(IVacationOrderService.class);
//        IFlowInfoService flowInfoService = SpringUtils.getBean(IFlowInfoService.class);
//        FlowMain flowMain = flowInfoService.queryFlowById(Long.valueOf(flowInstId));
//        vacationOrderService.updateState(flowMain.getOrderNo(), SysConstant.COMPLETED_STATE);
        log.info("审批完成:{}", execution);
    }
}
