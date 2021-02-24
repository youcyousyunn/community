package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiProcessLogDao;
import com.ycs.community.activiti.domain.dto.ActivitiProcessLogResponseDto;
import com.ycs.community.activiti.domain.po.ProcessLog;
import com.ycs.community.activiti.service.ActivitiProcessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ActivitiProcessLogServiceImpl implements ActivitiProcessLogService {
    @Autowired
    private ActivitiProcessLogDao activitiProcessLogDao;


    @Override
    public ActivitiProcessLogResponseDto qryOperLog(Long processId) {
        List<ProcessLog> data = activitiProcessLogDao.qryOperLog(processId);
        ActivitiProcessLogResponseDto responseDto = new ActivitiProcessLogResponseDto();
        if(!CollectionUtils.isEmpty(data)) {
            responseDto.setData(data);
        }
        return responseDto;
    }
}
