package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.QuartzJobDao;
import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageResponseDto;
import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import com.ycs.community.sysbo.quartz.QuartzManage;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuartzJobServiceImpl implements QuartzJobService {
    @Autowired
    private QuartzJobDao quartzJobDao;
    @Autowired
    private QuartzManage quartzManage;

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
    public boolean executeJob(Long id) {
        QuartzJobPo quartzJobPo = quartzJobDao.qryJobById(id);
        if (!StringUtils.isEmpty(quartzJobPo)) {
            quartzManage.executeJob(quartzJobPo);
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.JOB_NOT_EXIST, "定时任务不存在");
        }
        return true;
    }

    @Override
    public List<QuartzJobPo> qryJobByRunning() {
        List<QuartzJobPo> quartzJobPoList = quartzJobDao.qryJobByRunning();
        return quartzJobPoList;
    }

    @Override
    public boolean updJobStatus(QuartzJobPo quartzJobPo) {
        if (quartzJobPo.getIsPause()) {
            quartzManage.resumeJob(quartzJobPo);
            quartzJobPo.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJobPo);
            quartzJobPo.setIsPause(true);
        }
        int result = quartzJobDao.updJobStatus(quartzJobPo);
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override
    public QuartzJobPo qryJobById(Long id) {
        QuartzJobPo quartzJobPo = quartzJobDao.qryJobById(id);
        if (!StringUtils.isEmpty(quartzJobPo)) {
            quartzManage.executeJob(quartzJobPo);
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.JOB_NOT_EXIST, "定时任务不存在");
        }
        return quartzJobPo;
    }

    @Override
    @Transactional(readOnly = true)
    public QryQuartzJobPageResponseDto qryQuartzPage(QryQuartzJobPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = quartzJobDao.qryQuartzCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<QuartzJobPo> data = quartzJobDao.qryQuartzPage(paramMap);
        // 组装分页信息
        QryQuartzJobPageResponseDto response = new QryQuartzJobPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }
}
