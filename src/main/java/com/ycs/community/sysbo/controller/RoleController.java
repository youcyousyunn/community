package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.QryRolePageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRolePageResponseDto;
import com.ycs.community.sysbo.domain.dto.RoleRequestDto;
import com.ycs.community.sysbo.domain.dto.RoleResponseDto;
import com.ycs.community.sysbo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     * @param request
     * @return
     */
    @GetMapping("/role/queryPage")
    public QryRolePageResponseDto qryRolePage(QryRolePageRequestDto request) {
        QryRolePageResponseDto responsePageDto = new QryRolePageResponseDto();
        responsePageDto = roleService.qryRolePage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 新增角色
     * @param request
     * @return
     */
    @PostMapping("/role")
    public RoleResponseDto addRole(@RequestBody RoleRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        RoleResponseDto responseDto = new RoleResponseDto();
        roleService.addRole(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/role/{id}")
    public RoleResponseDto delRole(@PathVariable("id") Long id) {
        RoleResponseDto responseDto = new RoleResponseDto();
        roleService.delRole(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

   /**
     * 更新角色
     * @param request
     * @return
     */
    @PutMapping("/role")
    @PreAuthorize("hasPermission('role:upd')")
    public RoleResponseDto updRole(@RequestBody RoleRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        RoleResponseDto responseDto = new RoleResponseDto();
        roleService.updRole(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新角色菜单
     * @param request
     * @return
     */
    @PutMapping("/role/menu")
    public RoleResponseDto updRoleMenu(@RequestBody RoleRequestDto request) {
        // 接口请求报文检查
        if (null == request.getId() || StringUtils.isEmpty(request.getId())) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        RoleResponseDto responseDto = new RoleResponseDto();
        roleService.updRoleMenu(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 查询单个角色菜单
     * @param id
     * @return
     */
    @GetMapping("/role/menu/{id}")
    public RoleResponseDto qryRoleMenu(@PathVariable("id") Long id) {
        RoleResponseDto responseDto = new RoleResponseDto();
        responseDto = roleService.qryRoleMenuById(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
