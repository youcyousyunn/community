package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.RolePo;
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
    @Autowired
    private RoleDao roleDao;

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
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DEPT_FAIL, "修改部门失败");
        }
        return true;
    }
}