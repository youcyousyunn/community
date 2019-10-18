package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.RedisPo;
import lombok.Data;

import java.util.List;

@Data
public class QryRedisPageResponseDto extends BaseResponseDto {
    private List<RedisPo> data;
    private Integer total;
}
