package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class TagRequestDto extends BaseRequestDto {
    private Long id;
    private String name;
    private Long pid;
    private String description;
    private String icon;
    private int star;
    private int order;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(pid)) {
            return false;
        }
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
}