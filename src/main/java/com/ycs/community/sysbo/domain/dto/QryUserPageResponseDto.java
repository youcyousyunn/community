package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.UserPo;
import lombok.Data;

import java.util.List;

@Data
public class QryUserPageResponseDto extends BaseResponseDto {
    private List<UserPo> data;
    private Integer total;
}
