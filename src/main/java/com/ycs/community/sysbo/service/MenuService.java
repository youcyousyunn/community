package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;
import com.ycs.community.sysbo.domain.po.MenuPo;

import java.util.List;
import java.util.Map;

public interface MenuService {
    MenuResponseDto qryMenu(MenuRequestDto request);
    List<MenuPo> qryMenusByPid(Long pid);
    List<Map<String,Object>> qryAllMenu(List<MenuPo> menus);
    MenuResponseDto qryMenuTree(MenuRequestDto request);
    boolean delMenu(Long id);
    boolean addMenu(MenuRequestDto request);
    boolean updMenu(MenuRequestDto request);
}
