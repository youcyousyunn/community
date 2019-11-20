package com.ycs.community.sysbo.service;

import com.ycs.community.cmmbo.domain.po.UserPo;

import java.util.List;

public interface UserService {
    boolean addOrUpdateUser(UserPo userPo);
    UserPo qryUserById(long id);
    UserPo qryUserInfoByName(String userNm);
    List<String> qryRolesByUserId(Long userId);
}
