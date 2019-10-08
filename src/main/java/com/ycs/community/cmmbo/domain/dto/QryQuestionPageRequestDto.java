package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import lombok.Data;

import java.util.List;

@Data
public class QryQuestionPageRequestDto extends BaseRequestDto {
    private Integer currentPage;
    private Integer pageSize;
}
