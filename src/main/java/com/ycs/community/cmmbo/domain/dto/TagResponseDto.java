package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;
import lombok.Data;

import java.util.List;

@Data
public class TagResponseDto extends BaseResponseDto {
    private List<TagPo> data;
}
