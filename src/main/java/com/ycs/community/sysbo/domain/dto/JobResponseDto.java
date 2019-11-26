package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.JobPo;
import lombok.Data;

@Data
public class JobResponseDto extends BaseResponseDto {
    private JobPo data;
}
