package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDao {
    List<MenuPo> qryMenusByRole(List<RolePo> rolePoList);
    List<MenuPo> qryMenusByRoleId(Long roleId);
    List<MenuPo> qryMenusByPid(Long pid);
}
