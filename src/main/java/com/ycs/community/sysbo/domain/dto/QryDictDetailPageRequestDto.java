package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class QryDictDetailPageRequestDto extends BaseRequestDto {
    private Long dictId;
    private String label;
    private Integer currentPage;
    private Integer pageSize;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(String.valueOf(dictId))) {
            return false;
        }
        return true;
    }
}
