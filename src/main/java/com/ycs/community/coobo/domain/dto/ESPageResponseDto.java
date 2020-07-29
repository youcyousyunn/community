package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.coobo.domain.po.JDDocumentPo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ESPageResponseDto extends BaseResponseDto {
    private List<Map<String, Object>> data;
    private Integer total;
}
