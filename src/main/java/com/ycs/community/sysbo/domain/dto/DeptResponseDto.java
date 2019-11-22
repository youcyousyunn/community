package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import lombok.Data;

import java.util.List;

@Data
public class DeptResponseDto extends BaseResponseDto {
    private List<DeptPo> data;
}
