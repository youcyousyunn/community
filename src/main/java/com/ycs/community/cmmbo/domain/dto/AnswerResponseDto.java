package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import lombok.Data;

import java.util.List;

@Data
public class AnswerResponseDto extends BaseResponseDto {
    private List<AnswerPo> data;
}
