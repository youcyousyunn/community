package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.dao.UserDao;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.cmmbo.service.UserService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.po.RolePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public boolean addOrUpdateUser(UserPo userPo) {
        // 查询数据库用户是否存在
        UserPo user = userDao.qryUserByAccountId(userPo.getAccountId());
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
    public UserPo qryUserByAccountId(long accountId) {
        UserPo userPo = userDao.qryUserByAccountId(accountId);
        if (!StringUtils.isEmpty(userPo)) {
            // 根据用户ID获取角色
            List<RolePo> roleList = roleDao.qryRolesByUserId(userPo.getId());
            if (!StringUtils.isEmpty(roleList)) {
                List<String> roles = new ArrayList<>();
                for (RolePo rolePo : roleList) {
                    roles.add(rolePo.getCode());
                }
                userPo.setRoles(roles);
            }
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
    public List<String> qryRolesByUserId(Long userId) {
        List<RolePo> roleList = roleDao.qryRolesByUserId(userId);
        if (!StringUtils.isEmpty(roleList)) {
            List<String> roles = new ArrayList<>();
            for (RolePo rolePo : roleList) {
                roles.add(rolePo.getCode());
            }
            return roles;
        }
        return null;
    }
}
