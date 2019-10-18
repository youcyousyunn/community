package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;

@Data
public class QuartzJobRequestDto extends BaseRequestDto {
    private Long id;
    private String name;
    private String cron;
    private boolean status;
    private String remark;
}
