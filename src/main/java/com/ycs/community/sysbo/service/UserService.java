package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryUserPageResponseDto;
import com.ycs.community.sysbo.domain.dto.UserRequestDto;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.domain.po.UserPo;

import java.util.List;

public interface UserService {
    boolean addOrUpdateUser(UserPo userPo);
    UserPo qryUserById(long id);
    UserPo qryUserInfoByName(String userNm);
    List<RolePo> qryRolesByUserId(Long userId);
    QryUserPageResponseDto qryUserPage(QryUserPageRequestDto request);
    boolean updUser(UserRequestDto request);
    boolean delUser(Long id);
    boolean addUser(UserRequestDto request);
}
