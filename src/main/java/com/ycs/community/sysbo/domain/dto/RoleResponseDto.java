package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.RolePo;
import lombok.Data;

@Data
public class RoleResponseDto extends BaseResponseDto {
    private RolePo data;
}
