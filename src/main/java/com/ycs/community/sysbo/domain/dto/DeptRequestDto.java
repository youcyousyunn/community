package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class DeptRequestDto extends BaseRequestDto {
    private Long id;
    private Long pid;
    private String name;
    private Boolean enabled;
    private List<Long> ids;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(String.valueOf(id))) {
            return false;
        }
        if (StringUtils.isEmpty(String.valueOf(pid))) {
            return false;
        }
        if (StringUtils.isEmpty(enabled)) {
            return false;
        }
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
}
