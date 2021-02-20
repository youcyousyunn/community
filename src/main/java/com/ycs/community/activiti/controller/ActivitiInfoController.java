package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.service.ActivitiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiInfoController {
    @Autowired
    private ActivitiInfoService activitiInfoService;
}
