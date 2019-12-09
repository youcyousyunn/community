package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import lombok.Data;

import java.util.List;

@Data
public class QryOnlineUserPageResponseDto extends BaseResponseDto {
    private List<OnlineUserPo> data;
    private Integer total;
}
