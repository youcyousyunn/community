package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class QryLogPageRequestDto extends BaseRequestDto {
    private String type;
    private String userNm;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer currentPage;
    private Integer pageSize;
}
