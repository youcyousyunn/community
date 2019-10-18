package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;

@Data
public class QryRedisPageRequestDto extends BaseRequestDto {
    private String key;
    private Integer currentPage;
    private Integer pageSize;
}
