package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class UserRequestDto extends BaseRequestDto {
    private String name;
    private String password;
    private String code;
    private String uuid;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == name || StringUtils.isEmpty(name)) {
            return false;
        }
        if (null == password || StringUtils.isEmpty(password)) {
            return false;
        }
        if (null == code || StringUtils.isEmpty(code)) {
            return false;
        }
        if (null == uuid || StringUtils.isEmpty(uuid)) {
            return false;
        }
        return true;
    }
}
