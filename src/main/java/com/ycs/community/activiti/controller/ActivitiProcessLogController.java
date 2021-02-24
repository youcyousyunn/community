package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.service.ActivitiTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiProcessLogController {
    @Autowired
    private ActivitiTaskService activitiTaskService;


}
