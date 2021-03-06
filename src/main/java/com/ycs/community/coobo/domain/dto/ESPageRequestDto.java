package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ESPageRequestDto extends BaseRequestDto {
    private String keyword;
    private Integer currentPage;
    private Integer pageSize;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if ("" == keyword || null == keyword) {
            return false;
        }
        return true;
    }
}
