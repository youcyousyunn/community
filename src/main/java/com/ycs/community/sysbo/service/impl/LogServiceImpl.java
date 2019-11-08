package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.coobo.domain.dto.QryAttachPageResponseDto;
import com.ycs.community.coobo.domain.po.AttachPo;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.LogDao;
import com.ycs.community.sysbo.domain.dto.QryLogPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryLogPageResponseDto;
import com.ycs.community.sysbo.domain.po.LogJnlPo;
import com.ycs.community.sysbo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDao logDao;

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addLog(LogJnlPo logJnlPo) {
        int result = logDao.addLog(logJnlPo);
        if (result == 1) {
            return true;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_LOG_FAIL, "添加日志失败");
        }
    }

    @Override
    public QryLogPageResponseDto qryLogPage(QryLogPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", request.getType());
        paramMap.put("userNm", request.getUserNm());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = logDao.qryLogCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<LogJnlPo> data = logDao.qryLogPage(paramMap);
        // 组装分页信息
        QryLogPageResponseDto response = new QryLogPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }
}