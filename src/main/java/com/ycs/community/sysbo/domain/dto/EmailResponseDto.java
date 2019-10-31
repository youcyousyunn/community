package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.email.EmailConfig;
import lombok.Data;

@Data
public class EmailResponseDto extends BaseResponseDto {
    private EmailConfig data;
}
