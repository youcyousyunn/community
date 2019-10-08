package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.UserResponseDto;
import com.ycs.community.cmmbo.service.UserService;
import com.ycs.community.spring.context.CmmSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询用户信息
     * @param request
     * @return
     */
    @GetMapping("/user/qryUserInfo")
    public UserResponseDto qryUserInfo(HttpServletRequest request) {
        UserResponseDto responseDto = new UserResponseDto();
        String sessionId = request.getHeader(Constants.AUTH_TOKEN);
        CmmSessionContext instance = CmmSessionContext.getInstance();
        HttpSession session = instance.getSession(sessionId);
        if (!StringUtils.isEmpty(session)) {
            responseDto = userService.qryUserByAccountId((Long) session.getAttribute(sessionId));
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
