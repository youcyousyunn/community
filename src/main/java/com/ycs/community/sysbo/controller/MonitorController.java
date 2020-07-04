package com.ycs.community.sysbo.controller;

import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.sysbo.service.MonitorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统监控: 服务监控")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    /**
     * 获取服务器信息
     * @return
     */
    @GetMapping("/monitor")
    @AnonymousAccess
    public ResponseEntity<Object> qryServer() {
        return new ResponseEntity<>(monitorService.qryServer(), HttpStatus.OK);
    }
}
