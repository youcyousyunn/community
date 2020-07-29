package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.coobo.domain.po.JDContentPo;
import lombok.Data;

import java.util.List;

@Data
public class ESResponseDto extends BaseResponseDto {
    private List<JDContentPo> data;
}
