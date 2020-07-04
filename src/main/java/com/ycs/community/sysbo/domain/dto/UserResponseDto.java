package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.UserPo;
import lombok.Data;

@Data
public class UserResponseDto extends BaseResponseDto {
    private UserPo data;
    private String token;
    private String redirectUrl;
}
