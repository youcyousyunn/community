package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.dao.MenuDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;
import com.ycs.community.sysbo.domain.po.MenuMetaPo;
import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public MenuResponseDto qryMenu(MenuRequestDto request) {
        Long userId = SecurityUtil.getUserId();
        List<RolePo> rolePoList = roleDao.qryRolesByUserId(userId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", request.getType());
        paramMap.put("rolePoList", rolePoList);
        List<MenuPo> menuPoList = menuDao.qryMenusByRole(paramMap);
        // 构建菜单树
        List<MenuPo> menuTree = new LinkedList<>();
        for (MenuPo menuPo : menuPoList) {
            if (0 == menuPo.getPid()) {
                menuTree.add(menuPo);
            }

            for (MenuPo item : menuPoList) {
                if (item.getPid().equals(menuPo.getId())) {
                    if (CollectionUtils.isEmpty(menuPo.getChildren())) {
                        menuPo.setChildren(new ArrayList<MenuPo>());
                    }
                    menuPo.getChildren().add(item);
                }
            }
        }
        MenuResponseDto response = new MenuResponseDto();
        if (!CollectionUtils.isEmpty(menuTree)) {
            menuTree = buildMenu(menuTree);
            response.setData(menuTree);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> qryAllMenu(List<MenuPo> menus) {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
            List<MenuPo> menuList = menuDao.qryMenusByPid(menu.getId());
            Map<String,Object> map = new HashMap<>();
            map.put("id",menu.getId());
            map.put("label",menu.getName());
            if (!CollectionUtils.isEmpty(menuList)) {
                map.put("children",qryAllMenu(menuList));
            }
            list.add(map);
        });
        return list;
    }

    @Override
    public List<MenuPo> qryMenusByPid(Long pid) {
        List<MenuPo> menus = menuDao.qryMenusByPid(pid);
        return menus;
    }

    /**
     * 组装成VUE菜单结构
     * @param menuTree
     * @return
     */
    private List<MenuPo> buildMenu(List<MenuPo> menuTree) {
        List<MenuPo> result = new LinkedList<>();
        menuTree.forEach(item -> {
            List<MenuPo> children = item.getChildren();
            MenuPo menuPo = new MenuPo();
            menuPo.setId(item.getId());
            menuPo.setPid(item.getPid());
            menuPo.setName(item.getName());
            // 一级目录需要加斜杠，不然会报警告
            menuPo.setPath(item.getPid() == 0 ? "/" + item.getPath() :item.getPath());
            menuPo.setCache(item.isCache());
            menuPo.setHidden(item.isHidden());
            // 非外链
            if (!item.isIFrame()) {
                // 判断是否是一级目录
                if (item.getPid() == 0){
                    menuPo.setComponent(StringUtils.isEmpty(item.getComponent()) ? "Layout" : item.getComponent());
                } else {
                    menuPo.setComponent(item.getComponent());
                }
            }

            // 填充meta
            menuPo.setMeta(new MenuMetaPo(item.getName(), item.getIcon(), !item.isCache()));

            // 有子菜单的情况
            if (!CollectionUtils.isEmpty(children)) {
                menuPo.setAlwaysShow(true);
                menuPo.setRedirect("noRedirect");
                menuPo.setChildren(buildMenu(children));
                // 处理是一级菜单并且没有子菜单的情况
            } else if (item.getPid() == 0) {
                MenuPo childrenMenu = new MenuPo();
                childrenMenu.setMeta(menuPo.getMeta());
                // 判断是否为外链
                if (!item.isIFrame()) {
                    childrenMenu.setPath("index");
                    childrenMenu.setName(menuPo.getName());
                    childrenMenu.setComponent(menuPo.getComponent());
                } else {
                    childrenMenu.setPath(menuPo.getPath());
                }

                menuPo.setName(null);
                menuPo.setMeta(null);
                menuPo.setComponent("Layout");
                List<MenuPo> childrenMenuList = new ArrayList<>();
                childrenMenuList.add(childrenMenu);
                menuPo.setChildren(childrenMenuList);
                menuPo.setCreTm(item.getCreTm());
            }
            result.add(menuPo);
        });

        return result;
    }

    @Override
    public MenuResponseDto qryMenuTree(MenuRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        Long userId = SecurityUtil.getUserId();
        List<RolePo> rolePoList = roleDao.qryRolesByUserId(userId);
        paramMap.put("rolePoList", rolePoList);
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        List<MenuPo> menuPoList = menuDao.qryMenus(paramMap);
        // 构建菜单树
        List<MenuPo> menuTree = new LinkedList<>();
        for (MenuPo menuPo : menuPoList) {
            if (0 == menuPo.getPid()) {
                menuTree.add(menuPo);
            }

            for (MenuPo item : menuPoList) {
                if (item.getPid().equals(menuPo.getId())) {
                    if (CollectionUtils.isEmpty(menuPo.getChildren())) {
                        menuPo.setChildren(new ArrayList<MenuPo>());
                    }
                    menuPo.getChildren().add(item);
                }
            }
        }
        MenuResponseDto response = new MenuResponseDto();
        if (!CollectionUtils.isEmpty(menuTree)) {
            response.setData(menuTree);
        } else {
            response.setData(menuPoList);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addMenu(MenuRequestDto request) {
        MenuPo menuPo = BeanUtil.trans2Entity(request, MenuPo.class);
        menuPo.setCreTm(new Date().getTime());
        if (menuDao.addMenu(menuPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_MENU_FAIL, "添加菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delMenu(Long id) {
        // 删除菜单前判断是否存在子菜单
        List<MenuPo> children = menuDao.qryMenusByPid(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.HAS_CHILDREN_CAN_NOT_DEL_MENU, "存在子菜单, 不能删除");
        }
        // 删除菜单前先删除与该菜单有关联的角色菜单
        if (roleDao.delRoleMenuByMenuId(id) < 0) { // 该菜单可能没有角色关联
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ROLE_MENU_FAIL, "删除角色菜单失败");
        }
        // 删除菜单
        if (menuDao.delMenu(id) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_MENU_FAIL, "删除菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updMenu(MenuRequestDto request) {
        if(request.getId().equals(request.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        MenuPo menuPo = BeanUtil.trans2Entity(request, MenuPo.class);
        menuPo.setUpdTm(new Date().getTime());
        if (menuDao.updMenu(menuPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_MENU_FAIL, "更新菜单失败");
        }
        return true;
    }
}
