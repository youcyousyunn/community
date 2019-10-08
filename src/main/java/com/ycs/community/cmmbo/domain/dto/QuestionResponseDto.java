package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.domain.po.TopicPo;
import lombok.Data;

import java.util.List;

@Data
public class QuestionResponseDto extends BaseResponseDto {
    private QuestionPo data;
}
