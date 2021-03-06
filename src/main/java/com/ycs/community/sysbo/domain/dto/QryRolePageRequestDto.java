package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class QryRolePageRequestDto extends BaseRequestDto {
    private List<Long> ids;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer currentPage;
    private Integer pageSize;
}
