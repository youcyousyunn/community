package com.ycs.community.sysbo.service.impl;

import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.dao.MenuDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public boolean hasPermission(String permission) {
        Long userId = SecurityUtil.getUserId();
        List<RolePo> rolePoList = roleDao.qryRolesByUserId(userId);
        if (CollectionUtils.isEmpty(rolePoList)) {
            return false;
        } else {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("rolePoList", rolePoList);
            List<MenuPo> menuPoList = menuDao.qryMenusByRole(paramMap);
            if (CollectionUtils.isEmpty(menuPoList)) {
                return false;
            }
            List<Long> menuList = menuPoList.stream().map(MenuPo:: getId).collect(Collectors.toList());
            MenuPo menuPo = menuDao.qryMenu(permission);
            if (StringUtils.isEmpty(menuPo)) {
                return false;
            } else {
                return menuList.contains(menuPo.getId());
            }
        }
    }
}