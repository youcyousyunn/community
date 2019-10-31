package com.ycs.community.sysbo.service.impl;

import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.sysbo.dao.MenuDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;
import com.ycs.community.sysbo.domain.po.MenuMetaPo;
import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.service.MenuService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        UserPo userPo = new UserPo();
        userPo.setId(1l);
        List<RolePo> rolePoList = roleDao.qryRoleByUserId(userPo.getId());
        List<MenuPo> menuPoList = menuDao.qryMenuByRole(rolePoList);
        // 构建菜单树
        List<MenuPo> menuTree = new ArrayList<>();
        for (MenuPo menuPo : menuPoList) {
            if ("0".equals(String.valueOf(menuPo.getPid()))) {
                menuTree.add(menuPo);
            }

            for (MenuPo item : menuPoList) {
                if (item.getPid().equals(menuPo.getId())) {
                    if (StringUtils.isEmpty(menuPo.getChildren())) {
                        menuPo.setChildren(new ArrayList<MenuPo>());
                    }
                    menuPo.getChildren().add(item);
                }
            }
        }
        MenuResponseDto response = new MenuResponseDto();
        if (!StringUtils.isEmpty(menuTree)) {
            menuTree = buildMenu(menuTree);
            response.setData(menuTree);
        }
        return response;
    }

    /**
     * 组装成VUE菜单结构
     * @param menuTree
     * @return
     */
    private List<MenuPo> buildMenu(List<MenuPo> menuTree) {
        List<MenuPo> result = new LinkedList<>();
        menuTree.forEach(item -> {
                    if (!StringUtils.isEmpty(item)) {
                        List<MenuPo> children = item.getChildren();
                        MenuPo menuPo = new MenuPo();
                        menuPo.setName(item.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuPo.setPath(item.getPid() == 0 ? "/" + item.getPath() :item.getPath());
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
                        if (!StringUtils.isEmpty(children)) {
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
                        }
                        result.add(menuPo);
                    }
                }
        );

        return result;
    }
}