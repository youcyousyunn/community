package com.ycs.community.spring.security.service;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.cmmbo.service.UserService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userNm) throws UsernameNotFoundException {
        UserPo userPo = userService.qryUserInfoByName(userNm);
        if (StringUtils.isEmpty(userPo)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.USER_NOT_EXIST, "用户不存在");
        }
        return userPo;
    }
}