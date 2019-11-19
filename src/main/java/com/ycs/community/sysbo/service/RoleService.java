package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryRolePageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRolePageResponseDto;
import com.ycs.community.sysbo.domain.dto.RoleRequestDto;

public interface RoleService {
    QryRolePageResponseDto qryRolePage(QryRolePageRequestDto request);
    boolean updRoleMenu(RoleRequestDto request);
    boolean updRole(RoleRequestDto request);
    boolean addRole(RoleRequestDto request);
}
