package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ESRequestDto extends BaseRequestDto {
    private String keyword;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == keyword || StringUtils.isEmpty(keyword)) {
            return false;
        }
        return true;
    }
}
