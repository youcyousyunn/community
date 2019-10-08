package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import lombok.Data;

import java.util.List;

@Data
public class QryQuestionPageResponseDto  extends BaseResponseDto {
    private List<QuestionPo> data;
    private Integer total;
}
