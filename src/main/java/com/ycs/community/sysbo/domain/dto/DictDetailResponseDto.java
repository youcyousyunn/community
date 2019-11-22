package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.DictPo;
import lombok.Data;

import java.util.List;

@Data
public class DictDetailResponseDto extends BaseResponseDto {
    private List<DictPo> data;
}
