package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class QryActivitiFlowRequestDto extends BaseRequestDto {
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
