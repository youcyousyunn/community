package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.UserResponseDto;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询用户信息
     * @param request
     * @return
     */
    @GetMapping("/info")
    @OperationLog(title = "获取用户信息", action = OperationType.GET, isSave = true, channel = "web")
    public UserResponseDto qryUserInfo (HttpServletRequest request) {
        UserResponseDto responseDto = new UserResponseDto();
        UserPo userPo = userService.qryUserById(SecurityUtil.getUserId());
        responseDto.setData(userPo);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
