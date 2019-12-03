package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.JobDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private JobDao jobDao;

    @Override
    public DeptResponseDto qryDeptTree(DeptRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(request.getIds())) {
            paramMap.put("ids", request.getIds());
        }
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
        List<DeptPo> noRepeatDeptTree = new LinkedList<>();
        List<Long> deptIds = deptPoList.stream().map(DeptPo :: getId).collect(Collectors.toList());
        deptPoList.forEach(deptPo -> {
            boolean isChild = false;
            if (0 == deptPo.getPid()) {
                deptTree.add(deptPo);
            }
            for (DeptPo dept : deptPoList) {
                if (dept.getPid().equals(deptPo.getId())) {
                    isChild = true;
                    if (CollectionUtils.isEmpty(deptPo.getChildren())) {
                        deptPo.setChildren(new ArrayList<DeptPo>());
                    }
                    deptPo.getChildren().add(dept);
                }
            }
            if (isChild) {
                noRepeatDeptTree.add(deptPo);
            } else if (!deptIds.contains(deptDao.qryDeptById(deptPo.getPid()).getId())) {
                noRepeatDeptTree.add(deptPo);
            }
        });
        DeptResponseDto response = new DeptResponseDto();
        if (!CollectionUtils.isEmpty(deptTree)) {
            response.setData(deptTree);
        } else {
            response.setData(noRepeatDeptTree);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addDept(DeptRequestDto request) {
        DeptPo deptPo = BeanUtil.trans2Entity(request, DeptPo.class);
        deptPo.setCreTm(new Date().getTime());
        if (deptDao.addDept(deptPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_DEPT_FAIL, "添加部门失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delDept(Long id) {
        // 删除部门前判断有没有子部门
        List<DeptPo> children = deptDao.qryDeptsByPid(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.HAS_CHILDREN_CAN_NOT_DEL_DEPT, "存在子部门, 不能删除");
        }
        // 删除部门前判断有没有关联的岗位
        List<JobPo> jobPoList = jobDao.qryJobsByDeptId(id);
        if (!CollectionUtils.isEmpty(jobPoList)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.RELATED_JOB_CAN_NOT_DEL_DEPT, "存在岗位关联, 请取消关联后再试");
        }
        // 删除部门前判断有没有关联的角色
        List<RolePo> rolePoList = roleDao.qryRolesByDeptId(id);
        if (!CollectionUtils.isEmpty(rolePoList)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.RELATED_ROLE_CAN_NOT_DEL_DEPT, "存在角色关联, 请取消关联后再试");
        }
        if (deptDao.delDept(id) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_DEPT_FAIL, "删除部门失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, CustomizeBusinessException.class})
    public boolean updDept(DeptRequestDto request) {
        if(request.getId().equals(request.getPid())) {
            throw new BadRequestException("上级部门不能为自己");
        }
        DeptPo deptPo = BeanUtil.trans2Entity(request, DeptPo.class);
        deptPo.setUpdTm(new Date().getTime());
        if (deptDao.updDept(deptPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DEPT_FAIL, "更新部门失败");
        }
        return true;
    }

    @Override
    public List<DeptPo> qryDeptsByRoleId(Long roleId) {
        List<DeptPo> deptPoList = deptDao.qryDeptsByRoleId(roleId);
        return deptPoList;
    }

    @Override
    public List<DeptPo> qryDeptsByPid(Long pid) {
        List<DeptPo> deptPoList = deptDao.qryDeptsByPid(pid);
        return deptPoList;
    }
}