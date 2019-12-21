package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.security.service.OnlineUserService;
import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageResponseDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统监控: 在线用户")
public class OnlineUserController {
    @Autowired
    private OnlineUserService onlineUserService;

    /**
     * 分页查询在线用户列表
     * @param request
     * @return
     */
    @GetMapping("/online/queryPage")
    @OperationLog(title = "分页查询在线用户列表", action = OperationType.GET, isSave = true, channel = "web")
    public QryOnlineUserPageResponseDto qryOnlinePage(QryOnlineUserPageRequestDto request) {
        QryOnlineUserPageResponseDto responsePageDto = new QryOnlineUserPageResponseDto();
        responsePageDto = onlineUserService.qryOnlinePage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 踢出用户
     * @param key
     * @return
     */
    @DeleteMapping("/online/{key}")
    @PreAuthorize("@el.hasPermission('online:tick')")
    public ResponseEntity kickOut(@PathVariable("key") String key) {
        onlineUserService.kickOut(key);
        return new ResponseEntity(HttpStatus.OK);
    }
}