package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.RolePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RoleDao {
    List<RolePo> qryRolesByUserId(Long userId);
    int qryRoleCount(Map<String, Object> paramMap);
    List<RolePo> qryRolePage(Map<String, Object> paramMap);
    int delRoleMenus(Map<String, Object> paramMap);
    int addRoleMenus(Map<String, Object> paramMap);
    int addRole(RolePo rolePo);
    int updRole(RolePo rolePo);
    int delRole(Long id);
    RolePo qryRoleById(Long id);
    int delRoleDepts(Map<String, Object> paramMap);
    int addRoleDepts(Map<String, Object> paramMap);
    List<RolePo> qryRolesByDeptId(Long deptId);
}
