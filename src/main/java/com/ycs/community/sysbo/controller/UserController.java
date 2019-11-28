package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.domain.dto.QryUserPageResponseDto;
import com.ycs.community.sysbo.domain.dto.QryUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.UserRequestDto;
import com.ycs.community.sysbo.domain.dto.UserResponseDto;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加用户
     * @param request
     * @return
     */
    @PostMapping("/user")
    public UserResponseDto addUser(@RequestBody UserRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequest2Dto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        UserResponseDto responseDto = new UserResponseDto();
        userService.addUser(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public UserResponseDto delUser(@PathVariable("id") Long id) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(id)) {
            BizLogger.info("接口请求报文异常" + id);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        UserResponseDto responseDto = new UserResponseDto();
        userService.delUser(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新用户
     * @param request
     * @return
     */
    @PutMapping("/user")
    public UserResponseDto updUser(@RequestBody UserRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequest2Dto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        UserResponseDto responseDto = new UserResponseDto();
        userService.updUser(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}