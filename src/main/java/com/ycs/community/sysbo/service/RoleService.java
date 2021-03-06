package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryRolePageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRolePageResponseDto;
import com.ycs.community.sysbo.domain.dto.RoleRequestDto;
import com.ycs.community.sysbo.domain.dto.RoleResponseDto;
import com.ycs.community.sysbo.domain.po.RolePo;

import java.util.List;

public interface RoleService {
    QryRolePageResponseDto qryRolePage(QryRolePageRequestDto request);
    boolean updRoleMenu(RoleRequestDto request);
    boolean updRole(RoleRequestDto request);
    boolean addRole(RoleRequestDto request);
    boolean delRole(Long id);
    RoleResponseDto qryRoleMenuById(Long id);
    List<RolePo> qryAllRole(RoleRequestDto request);
    List<RolePo> qryRolesByUserId();
}
