package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;

import java.util.List;

public interface DeptService {
    DeptResponseDto qryDeptTree(DeptRequestDto request);
    boolean updDept(DeptRequestDto request);
    boolean delDept(Long id);
    boolean addDept(DeptRequestDto request);
    List<DeptPo> qryDeptsByRoleId(Long roleId);
    List<DeptPo> qryDeptsByPid(Long pid);
}
