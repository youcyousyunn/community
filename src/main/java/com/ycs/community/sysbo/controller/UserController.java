package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.domain.dto.QryUserPageResponseDto;
import com.ycs.community.sysbo.domain.dto.QryUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.UserResponseDto;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询用户信息
     * @param request
     * @return
     */
    @GetMapping("/user/info")
    @OperationLog(title = "获取用户信息", action = OperationType.GET, isSave = true, channel = "web")
    public UserResponseDto qryUserInfo (HttpServletRequest request) {
        UserResponseDto responseDto = new UserResponseDto();
        UserPo userPo = userService.qryUserById(SecurityUtil.getUserId());
        responseDto.setData(userPo);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 分页查询用户列表
     * @param request
     * @return
     */
    @GetMapping("/user/queryPage")
    public QryUserPageResponseDto qryUserPage(QryUserPageRequestDto request) {
        QryUserPageResponseDto responsePageDto = new QryUserPageResponseDto();
        responsePageDto = userService.qryUserPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }
}
