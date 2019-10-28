package com.ycs.community.sysbo.quartz;

import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class QuartzManage {
    private Logger logger = LoggerFactory.getLogger(QuartzManage.class);
    private static final String JOB_IDENTITY = "CMM_TASK";
    @Resource(name = "scheduler")
    private Scheduler scheduler;

    /**
     * 添加定时任务
     * @param quartzJobPo
     */
    public void addJob(QuartzJobPo quartzJobPo) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                    withIdentity(JOB_IDENTITY + quartzJobPo.getId()).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_IDENTITY + quartzJobPo.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJobPo.getCron()))
                    .build();

            cronTrigger.getJobDataMap().put(quartzJobPo.JOB_KEY, quartzJobPo);

            //重置启动时间
            ((CronTriggerImpl)cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 暂停任务
            if (quartzJobPo.getIsPause()) {
                pauseJob(quartzJobPo);
            }
        } catch (Exception e){
            logger.error("创建定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("创建定时任务失败");
        }
    }

    /**
     * 删除定时任务
     * @param quartzJobPo
     */
    public void deleteJob(QuartzJobPo quartzJobPo) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_IDENTITY + quartzJobPo.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e){
            logger.error("删除定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("删除定时任务失败");
        }
    }

    /**
     * 更新定时任务
     * @param quartzJobPo
     */
    public void updateJob(QuartzJobPo quartzJobPo) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_IDENTITY + quartzJobPo.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null){
                addJob(quartzJobPo);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJobPo.getCron());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 重置启动时间
            ((CronTriggerImpl)trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(QuartzJobPo.JOB_KEY,quartzJobPo);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (quartzJobPo.getIsPause()) {
                pauseJob(quartzJobPo);
            }
        } catch (Exception e){
            logger.error("更新定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("更新定时任务失败");
        }
    }

    /**
     * 执行定时任务
     * @param quartzJobPo
     */
    public void executeJob(QuartzJobPo quartzJobPo) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_IDENTITY + quartzJobPo.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(quartzJobPo);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(quartzJobPo.JOB_KEY, quartzJobPo);
            JobKey jobKey = JobKey.jobKey(JOB_IDENTITY + quartzJobPo.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e){
            logger.error("执行定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("执行定时任务失败");
        }
    }

    /**
     * 暂停定时任务
     * @param quartzJobPo
     */
    public void pauseJob(QuartzJobPo quartzJobPo) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_IDENTITY + quartzJobPo.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e){
            logger.error("暂停定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("暂停定时任务失败");
        }
    }

    /**
     * 恢复定时任务
     * @param quartzJobPo
     */
    public void resumeJob(QuartzJobPo quartzJobPo) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_IDENTITY + quartzJobPo.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(quartzJobPo);
            }
            JobKey jobKey = JobKey.jobKey(JOB_IDENTITY + quartzJobPo.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e){
            logger.error("恢复定时任务失败{}" + quartzJobPo, e);
            throw new BadRequestException("恢复定时任务失败");
        }
    }
}
