package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class ActivitiVacationTaskRequestDto extends BaseRequestDto {
    private Long id;
    private int type;
    private String title;
    private String context;
    private Long userId;
    private Long assignee;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private int state;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(title)) {
            return false;
        }
        if (StringUtils.isEmpty(startTime)) {
            return false;
        }
        if (StringUtils.isEmpty(endTime)) {
            return false;
        }
        return true;
    }
}
