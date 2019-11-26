package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.MenuDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.dto.QryRolePageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRolePageResponseDto;
import com.ycs.community.sysbo.domain.dto.RoleRequestDto;
import com.ycs.community.sysbo.domain.dto.RoleResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private DeptDao deptDao;

    @Override
    public QryRolePageResponseDto qryRolePage(QryRolePageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = roleDao.qryRoleCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<RolePo> data = roleDao.qryRolePage(paramMap);
        // 查询角色对应部门
        data.forEach(rolePo -> {
            List<DeptPo> deptPoList = deptDao.qryDeptsByRoleId(rolePo.getId());
            rolePo.setDepts(deptPoList);
        });
        // 查询角色对应菜单
        data.forEach(rolePo -> {
            List<MenuPo> menuPoList = menuDao.qryMenusByRoleId(rolePo.getId());
            rolePo.setMenus(menuPoList);
        });
        // 组装分页信息
        QryRolePageResponseDto response = new QryRolePageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addRole(RoleRequestDto request) {
        RolePo rolePo = BeanUtil.trans2Entity(request, RolePo.class);
        rolePo.setCreTm(new Date().getTime());
        if (roleDao.addRole(rolePo) < 0) { // 获取返回自增主键-到实体
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_ROLE_FAIL, "添加角色失败");
        } else {
            if (!CollectionUtils.isEmpty(rolePo.getDepts())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("roleId", rolePo.getId());
                List<Long> depts = rolePo.getDepts().stream().map(DeptPo :: getId).collect(Collectors.toList());
                paramMap.put("depts", depts);
                if (roleDao.addRoleDepts(paramMap) < 1) {
                    throw new CustomizeBusinessException(HiMsgCdConstants.ADD_ROLE_DEPT_FAIL, "添加角色部门失败");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delRole(Long id) {
        // 删除角色前先取消角色所拥有的部门
        if (deptDao.delDeptsByRoleId(id) < 0) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_DEPT_FAIL, "删除角色部门失败");
        } else {
            // 删除角色前先取消角色所拥有的菜单
            if (menuDao.delMenusByRoleId(id) < 0) { // 角色可能没有关联任何菜单
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_MENU_FAIL, "删除角色菜单失败");
            } else {
                if (roleDao.delRole(id) < 1) {
                    throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_FAIL, "删除角色失败");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updRole(RoleRequestDto request) {
        // 查询当前角色对应的部门
        List<DeptPo> deptPoList = deptDao.qryDeptsByRoleId(request.getId());
        List<DeptPo> depts = request.getDepts();
        List<Long> addList = new ArrayList<>(); // 新增部门列表
        List<Long> delList = new ArrayList<>(); // 删除部门列表
        addList = depts.stream().map(DeptPo :: getId).collect(Collectors.toList());
        List<Long> deptList = depts.stream().map(DeptPo :: getId).collect(Collectors.toList());
        for (DeptPo deptPo : deptPoList) {
            Long id = deptPo.getId();
            if (addList.contains(id)) {
                addList.remove(id);
            }
            if (!deptList.contains(id)) {
                delList.add(id);
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("roleId", request.getId());
        paramMap.put("depts", delList);
        if (!CollectionUtils.isEmpty(delList)) {
            if (roleDao.delRoleDepts(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_DEPT_FAIL, "删除角色部门失败");
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            paramMap.put("depts", addList);
            if (roleDao.addRoleDepts(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_ROLE_DEPT_FAIL, "添加角色部门失败");
            }
        }

        RolePo rolePo = BeanUtil.trans2Entity(request, RolePo.class);
        rolePo.setUpdTm(new Date().getTime());
        if (roleDao.updRole(rolePo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_ROLE_FAIL, "更新角色失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updRoleMenu(RoleRequestDto request) {
        // 查询当前角色对应的菜单
        List<MenuPo> menuPoList = menuDao.qryMenusByRoleId(request.getId());
        List<MenuPo> menus = request.getMenus();
        List<Long> addList = new ArrayList<>(); // 新增菜单列表
        List<Long> delList = new ArrayList<>(); // 删除菜单列表
        addList = menus.stream().map(MenuPo :: getId).collect(Collectors.toList());
        List<Long> menuList = menus.stream().map(MenuPo :: getId).collect(Collectors.toList());
        for (MenuPo menuPo : menuPoList) {
            Long id = menuPo.getId();
            if (addList.contains(id)) {
                addList.remove(id);
            }
            if (!menuList.contains(id)) {
                delList.add(id);
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("roleId", request.getId());
        paramMap.put("menus", delList);
        if (!CollectionUtils.isEmpty(delList)) {
            if (roleDao.delRoleMenus(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_MENU_FAIL, "删除角色菜单失败");
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            paramMap.put("menus", addList);
            if (roleDao.addRoleMenus(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_ROLE_MENU_FAIL, "添加角色菜单失败");
            }
        }
        return true;
    }

    @Override
    public RoleResponseDto qryRoleMenuById(Long id) {
        RolePo rolePo = roleDao.qryRoleById(id);
        if (!StringUtils.isEmpty(rolePo)) {
            List<MenuPo> menuPoList = menuDao.qryMenusByRoleId(rolePo.getId());
            if (!CollectionUtils.isEmpty(menuPoList)) {
                rolePo.setMenus(menuPoList);
            } else {
                throw new CustomizeBusinessException(HiMsgCdConstants.QRY_ROLE_MENU_FAIL, "查询角色菜单失败");
            }
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.QRY_ROLE_FAIL, "查询角色失败");
        }
        RoleResponseDto response = new RoleResponseDto();
        response.setData(rolePo);
        return response;
    }

    @Override
    public List<RolePo> qryAllRole(RoleRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        List<RolePo> result = roleDao.qryAllRole(paramMap);
        return result;
    }

    @Override
    public List<RolePo> qryRolesByUserId() {
        Long userId = SecurityUtil.getUserId();
        List<RolePo> roles = roleDao.qryRolesByUserId(userId);
        if (CollectionUtils.isEmpty(roles)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.QRY_USER_ROLE_FAIL, "查询用户角色失败");
        }
        return roles;
    }
}
