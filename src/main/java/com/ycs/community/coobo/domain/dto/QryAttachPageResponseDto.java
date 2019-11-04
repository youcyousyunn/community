package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.coobo.domain.po.AttachPo;
import lombok.Data;

import java.util.List;

@Data
public class QryAttachPageResponseDto extends BaseResponseDto {
    private List<AttachPo> data;
    private Integer total;
}
