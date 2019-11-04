package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;

@Data
public class QryAttachPageRequestDto extends BaseRequestDto {
    private Integer currentPage;
    private Integer pageSize;
    private String name;
}
