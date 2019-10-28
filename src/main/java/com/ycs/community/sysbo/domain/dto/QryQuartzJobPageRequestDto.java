package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;

@Data
public class QryQuartzJobPageRequestDto extends BaseRequestDto {
    private String name;
    private Integer currentPage;
    private Integer pageSize;
}
