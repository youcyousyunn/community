package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.UserPo;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto extends BaseResponseDto {
    private UserPo data;
    private String token;
}
