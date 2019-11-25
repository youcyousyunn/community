package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.DeptPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DeptDao {
    List<DeptPo> qryDeptTree(Map<String, Object> paramMap);
    int updDept(DeptPo deptPo);
    List<DeptPo> qryDeptsByRoleId(Long roleId);
    int delDeptsByRoleId(Long roleId);
}
