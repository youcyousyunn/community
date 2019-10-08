package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.UserResponseDto;
import com.ycs.community.cmmbo.domain.po.UserPo;

public interface UserService {
    boolean addOrUpdateUser(UserPo userPo);
    UserResponseDto qryUserByAccountId(long accountId);
}
