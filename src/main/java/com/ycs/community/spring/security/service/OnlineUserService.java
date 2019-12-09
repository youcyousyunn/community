package com.ycs.community.spring.security.service;

import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageResponseDto;
import com.ycs.community.sysbo.domain.po.UserPo;

import javax.servlet.http.HttpServletRequest;

public interface OnlineUserService {
    boolean saveOnlineUserInfo(UserPo userPo, String token, HttpServletRequest request);
    boolean logout(String token);
    QryOnlineUserPageResponseDto qryOnlinePage(QryOnlineUserPageRequestDto request);
    boolean kickOut(String key);
}