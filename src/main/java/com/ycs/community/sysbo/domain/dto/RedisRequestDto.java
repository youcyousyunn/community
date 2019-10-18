package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class RedisRequestDto extends BaseRequestDto {
    private String key;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == key || StringUtils.isEmpty(key)) {
            return false;
        }
        return true;
    }
}
