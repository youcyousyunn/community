package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.RolePo;
import lombok.Data;

import java.util.List;

@Data
public class QryRolePageResponseDto extends BaseResponseDto {
    private List<RolePo> data;
    private Integer total;
}
