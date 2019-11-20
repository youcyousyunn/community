package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.MenuPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MenuDao {
    List<MenuPo> qryMenusByRole(Map<String, Object> paramMap);
    List<MenuPo> qryMenusByRoleId(Long roleId);
    List<MenuPo> qryMenusByPid(Long pid);
    int delMenusByRoleId(Long id);
    MenuPo qryMenu(String permission);
}
