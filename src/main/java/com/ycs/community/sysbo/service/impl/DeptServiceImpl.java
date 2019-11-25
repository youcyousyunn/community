package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    @Override
    public DeptResponseDto qryDeptTree(DeptRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        paramMap.put("enabled", request.getEnabled());
        List<DeptPo> deptPoList = deptDao.qryDeptTree(paramMap);
        // 构建部门树
        List<DeptPo> deptTree = new LinkedList<>();
        deptPoList.forEach(deptPo -> {
            if (0 == deptPo.getPid()) {
                deptTree.add(deptPo);
            }
            for (DeptPo dept : deptPoList) {
                if (dept.getPid().equals(deptPo.getId())) {
                    if (CollectionUtils.isEmpty(deptPo.getChildren())) {
                        deptPo.setChildren(new ArrayList<DeptPo>());
                    }
                    deptPo.getChildren().add(dept);
                }
            }
        });
        DeptResponseDto response = new DeptResponseDto();
        if (!CollectionUtils.isEmpty(deptTree)) {
            response.setData(deptTree);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delDept(DeptRequestDto request) {

        return false;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, CustomizeBusinessException.class})
    public boolean updDept(DeptRequestDto request) {
        if(request.getId().equals(request.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        DeptPo deptPo = BeanUtil.trans2Entity(request, DeptPo.class);
        if (deptDao.updDept(deptPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DEPT_FAIL, "修改部门失败");
        }
        return true;
    }
}