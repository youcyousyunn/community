package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.po.UserPo;

import java.util.List;

public interface UserService {
    boolean addOrUpdateUser(UserPo userPo);
    UserPo qryUserByAccountId(long accountId);
    UserPo qryUserInfoByName(String userNm);
    List<String> qryRolesByUserId(Long userId);
}
