package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiTaskDao;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageResponseDto;
import com.ycs.community.activiti.domain.po.TaskPo;
import com.ycs.community.activiti.service.ActivitiTaskService;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiTaskServiceImpl implements ActivitiTaskService {
    @Autowired
    private ActivitiTaskDao activitiTaskDao;


    @Override
    public QryActivitiTaskPageResponseDto queryMyTaskPage(QryActivitiTaskPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        long userId = SecurityUtil.getUserId();
        paramMap.put("userId", userId);
        paramMap.put("state", Constants.REVIEW_STATE);
        if (!StringUtils.isEmpty(request.getName())) {
            paramMap.put("name", request.getName());
        }
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = activitiTaskDao.qryMyTaskCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<TaskPo> data = activitiTaskDao.qryMyTaskPage(paramMap);
        // 组装分页信息
        QryActivitiTaskPageResponseDto response = new QryActivitiTaskPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }
}
