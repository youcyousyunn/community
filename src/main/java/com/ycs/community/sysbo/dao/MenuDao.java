package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.MenuPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDao {
    List<MenuPo> qryMenuByRole(List<RolePo> rolePoList);
}
