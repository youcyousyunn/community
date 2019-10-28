package com.ycs.community.sysbo.quartz;

import com.ycs.community.spring.context.SpringContextHolder;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Async
public class ExecutionJob extends QuartzJobBean {
    @Autowired
    private QuartzJobService quartzJobService;
    private Logger logger = LoggerFactory.getLogger(ExecutionJob.class);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QuartzJobPo quartzJobPo = (QuartzJobPo) jobExecutionContext.getMergedJobDataMap().get(QuartzJobPo.JOB_KEY);
        // 获取 Spring bean
        QuartzManage quartzManage = SpringContextHolder.getBean("quartzManage");
        long startTime = System.currentTimeMillis();
        try {
            // 执行任务
            logger.info("任务准备执行，任务名称：{}", quartzJobPo.getName());
            QuartzRunnable task = new QuartzRunnable(quartzJobPo.getBeanNm(), quartzJobPo.getMethodNm(),
                    quartzJobPo.getParams());
            Future<?> future = executorService.submit(task);
            future.get();
            long costTime = System.currentTimeMillis() - startTime;
            logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJobPo.getName(), costTime);
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}" + quartzJobPo.getName(), e);
            quartzJobPo.setIsPause(false);
            // 更新状态
            quartzJobService.updJobStatus(quartzJobPo);
        }
    }
}
