package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.JobDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.dao.UserDao;
import com.ycs.community.sysbo.domain.dto.QryUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryUserPageResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private JobDao jobDao;

    @Override
    public boolean addOrUpdateUser(UserPo userPo) {
        // 查询数据库用户是否存在
        UserPo user = userDao.qryUserById(userPo.getId());
        if(StringUtils.isEmpty(user)) {
            userPo.setEnabled(true);
            userPo.setCreTm(new Date().getTime());
            int result = userDao.addUser(userPo);
            if (result != 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_USER_FAIL, "添加用户失败");
            }
        } else {
            user.setName(userPo.getName());
            user.setAvatarUrl(userPo.getAvatarUrl());
            user.setUpdTm(new Date().getTime());
            int result = userDao.updUser(user);
            if (result != 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.UPD_USER_FAIL, "更新用户失败");
            }
        }

        return true;
    }

    @Override
    public UserPo qryUserById(long id) {
        UserPo userPo = userDao.qryUserById(id);
        if (!StringUtils.isEmpty(userPo)) {
            // 根据用户ID获取角色
            List<RolePo> roleList = roleDao.qryRolesByUserId(userPo.getId());
            userPo.setRoles(roleList);
            return userPo;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.USER_NOT_EXIST, "用户不存在");
        }
    }

    @Override
    public UserPo qryUserInfoByName(String userNm) {
        UserPo userPo = userDao.qryUserInfoByName(userNm);
        if (StringUtils.isEmpty(userPo)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.USER_NOT_EXIST, "用户不存在");
        }
        return userPo;
    }

    @Override
    public List<RolePo> qryRolesByUserId(Long userId) {
        List<RolePo> roleList = roleDao.qryRolesByUserId(userId);
        if (!CollectionUtils.isEmpty(roleList)) {
            return roleList;
        }
        return null;
    }

    @Override
    public QryUserPageResponseDto qryUserPage(QryUserPageRequestDto request) {
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
        int totalCount = userDao.qryUserCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<UserPo> data = userDao.qryUserPage(paramMap);
        // 查询用户对应角色
        data.forEach(userPo -> {
            List<RolePo> roles = roleDao.qryRolesByUserId(userPo.getId());
            userPo.setRoles(roles);
        });
        // 查询用户对应部门
        data.forEach(userPo -> {
            DeptPo dept = deptDao.qryDeptByUserId(userPo.getId());
            userPo.setDept(dept);
        });
        // 查询用户对应岗位
        data.forEach(userPo -> {
            JobPo job = jobDao.qryJobByUserId(userPo.getId());
            userPo.setJob(job);
        });
        // 组装分页信息
        QryUserPageResponseDto response = new QryUserPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
        }
        return response;
    }
}
