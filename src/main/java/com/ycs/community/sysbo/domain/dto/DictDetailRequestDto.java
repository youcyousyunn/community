package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DictDetailRequestDto extends BaseRequestDto {
    private Long dictId;
    private Long id;
    private String label;
    private String value;
    private int sort;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(label)) {
            return false;
        }
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return true;
    }
}
