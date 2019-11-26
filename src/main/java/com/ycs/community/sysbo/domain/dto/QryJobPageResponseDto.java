package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.JobPo;
import lombok.Data;

import java.util.List;

@Data
public class QryJobPageResponseDto extends BaseResponseDto {
    private List<JobPo> data;
    private Integer total;
}
