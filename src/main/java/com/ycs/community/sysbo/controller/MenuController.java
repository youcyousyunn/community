package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;
import com.ycs.community.sysbo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 查询用户对应系统菜单
     * @param request
     * @return
     */
    @GetMapping("/menu")
    @OperationLog(title = "查询用户对应系统菜单", action = OperationType.GET, isSave = true, channel = "web")
    public MenuResponseDto qryMenu(MenuRequestDto request) {
        MenuResponseDto responseDto = new MenuResponseDto();
        responseDto = menuService.qryMenu(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
