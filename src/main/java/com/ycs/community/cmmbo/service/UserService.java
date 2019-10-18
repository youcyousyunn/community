package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.UserRequestDto;
import com.ycs.community.cmmbo.domain.dto.UserResponseDto;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.spring.exception.CustomizeBusinessException;

public interface UserService {
    boolean addOrUpdateUser(UserPo userPo);
    UserResponseDto qryUserByAccountId(long accountId);
    UserResponseDto login(UserRequestDto request) throws CustomizeBusinessException;
}
