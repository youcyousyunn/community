package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class DictRequestDto extends BaseRequestDto {
    private Long id;
    private String name;
    private String remark;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        if (StringUtils.isEmpty(remark)) {
            return false;
        }
        return true;
    }
}
