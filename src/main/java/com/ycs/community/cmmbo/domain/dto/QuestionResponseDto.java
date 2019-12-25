package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import lombok.Data;

@Data
public class QuestionResponseDto extends BaseResponseDto {
    private QuestionPo data;
}
