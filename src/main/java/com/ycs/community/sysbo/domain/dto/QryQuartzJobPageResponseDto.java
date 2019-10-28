package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import lombok.Data;

import java.util.List;

@Data
public class QryQuartzJobPageResponseDto extends BaseResponseDto {
    private List<QuartzJobPo> data;
    private Integer total;
}
