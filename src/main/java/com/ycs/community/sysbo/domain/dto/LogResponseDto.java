package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.LogJnlPo;
import lombok.Data;

@Data
public class LogResponseDto extends BaseResponseDto {
    private LogJnlPo data;
}
