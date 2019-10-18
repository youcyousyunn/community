package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.QuartzJobDao;
import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobServiceImpl implements QuartzJobService {
    private QuartzJobDao quartzJobDao;

    @Override
    public boolean addJob(QuartzJobRequestDto request) {
        if (!CronExpression.isValidExpression(request.getCron())) {
            throw new BadRequestException("QUARTZ定时任务CRON表达式格式错误");
        }
        QuartzJobPo quartzJobPo = BeanUtil.trans2Entity(request, QuartzJobPo.class);
        int result = quartzJobDao.addJob(quartzJobPo);
        if(result == 1) {
            return  true;
        }
        return false;
    }

    @Override
    public boolean updJobIsPause(Long id, Boolean isPause) {
        return false;
    }
}
