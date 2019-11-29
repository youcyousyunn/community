package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;
import com.ycs.community.sysbo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询全部菜单
     * @param pid
     * @return
     */
    @GetMapping("/menu/tree/{pid}")
    @OperationLog(title = "查询全部菜单", action = OperationType.GET, isSave = false, channel = "web")
    public ResponseEntity qryAllMenu(@PathVariable("pid") Long pid) {
        // 接口请求报文检查
        if (null == pid || StringUtils.isEmpty(pid)) {
            BizLogger.info("接口请求报文异常" + pid);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        List<Map<String, Object>> response = menuService.qryAllMenu(menuService.qryMenusByPid(pid));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 查询菜单树
     * @param request
     * @return
     */
    @GetMapping("/menu/tree")
    @OperationLog(title = "查询菜单树", action = OperationType.GET, isSave = false, channel = "web")
    public MenuResponseDto qryMenuTree(MenuRequestDto request) {
        MenuResponseDto responseDto = new MenuResponseDto();
        responseDto = menuService.qryMenuTree(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 添加菜单
     * @param request
     * @return
     */
    @PostMapping("/menu")
    public MenuResponseDto addMenu(@RequestBody MenuRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        MenuResponseDto responseDto = new MenuResponseDto();
        menuService.addMenu(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 根据ID删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("/menu/{id}")
    @OperationLog(title = "根据ID删除菜单", action = OperationType.GET, isSave = true, channel = "web")
    public MenuResponseDto delMenu(@PathVariable("id") Long id) {
        MenuResponseDto responseDto = new MenuResponseDto();
        menuService.delMenu(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新菜单
     * @param request
     * @return
     */
    @PutMapping("/menu")
    public MenuResponseDto updMenu(@RequestBody MenuRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        MenuResponseDto responseDto = new MenuResponseDto();
        menuService.updMenu(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
