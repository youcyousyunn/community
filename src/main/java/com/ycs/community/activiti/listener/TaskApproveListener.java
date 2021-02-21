package com.ycs.community.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务审批监听
 */
@Slf4j
public class TaskApproveListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("执行审批任务监听器ID:{},办理人:{}",delegateTask);
    }
}
