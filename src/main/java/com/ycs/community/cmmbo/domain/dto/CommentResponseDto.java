package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import lombok.Data;

import java.util.List;

@Data
public class CommentResponseDto extends BaseResponseDto {
    private List<CommentPo> data;
}
