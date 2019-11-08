package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.LogJnlPo;
import lombok.Data;

import java.util.List;

@Data
public class QryLogPageResponseDto extends BaseResponseDto {
    private List<LogJnlPo> data;
    private Integer total;
}
