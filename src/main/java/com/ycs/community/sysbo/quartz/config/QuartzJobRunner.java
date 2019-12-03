package com.ycs.community.sysbo.quartz.config;

import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import com.ycs.community.sysbo.quartz.QuartzManage;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuartzJobRunner implements ApplicationRunner {
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private QuartzManage quartzManage;
    private Logger logger = LoggerFactory.getLogger(QuartzJobRunner.class);

    /**
     * 项目启动时重新激活已启用的定时任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<QuartzJobPo> quartzJobPoList = quartzJobService.qryJobByRunning();
        logger.info("\n" + "注入定时任务开始......");
        for (QuartzJobPo quartzJobPo : quartzJobPoList ) {
            quartzManage.addJob(quartzJobPo);
        }
        logger.info("\n" + "------------------注入定时任务完成-------------------");
    }
}
