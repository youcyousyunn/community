package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.JobDao;
import com.ycs.community.sysbo.domain.dto.JobRequestDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobDao jobDao;
    @Autowired
    private DeptDao deptDao;

    @Override
    public QryJobPageResponseDto qryJobPage(QryJobPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        paramMap.put("enabled", request.getEnabled());
        // 查询总条数
        int totalCount = jobDao.qryJobCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<JobPo> data = jobDao.qryJobPage(paramMap);
        // 查询岗位对应部门
        data.forEach(jobPo -> {
            DeptPo deptPo = deptDao.qryDeptById(jobPo.getDeptPid());
            jobPo.setDeptSuperiorName(deptPo.getName());
            deptPo.setName(jobPo.getDeptName());
            jobPo.setDept(deptPo);
        });
        // 组装分页信息
        QryJobPageResponseDto response = new QryJobPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addJob(JobRequestDto request) {
        JobPo jobPo = BeanUtil.trans2Entity(request, JobPo.class);
        jobPo.setDeptId(jobPo.getDept().getId());
        jobPo.setCreTm(new Date().getTime());
        if (jobDao.addJob(jobPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_JOB_FAIL, "添加岗位失败");
        }
        return true;
    }

    @Override
    public boolean delJob(Long id) {
        if (jobDao.delJob(id) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_JOB_FAIL, "删除岗位失败");
        }
        return true;
    }

    @Override
    public boolean updJob(JobRequestDto request) {
        JobPo jobPo = BeanUtil.trans2Entity(request, JobPo.class);
        jobPo.setDeptId(jobPo.getDept().getId());
        jobPo.setUpdTm(new Date().getTime());
        if (jobDao.updJob(jobPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_JOB_FAIL, "更新岗位失败");
        }
        return true;
    }

    @Override
    public List<JobPo> qryJobsByDeptId(Long deptId) {
        List<JobPo> result = jobDao.qryJobsByDeptId(deptId);
        if (CollectionUtils.isEmpty(result)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.QRY_DEPT_JOB_FAIL, "查询部门下所有岗位失败");
        }
        return result;
    }
}