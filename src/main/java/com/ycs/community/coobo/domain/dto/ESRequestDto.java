package com.ycs.community.coobo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;

@Data
public class ESRequestDto extends BaseRequestDto {
    private String id;
    private String name;
    private String price;
    private String desc;
    private String img;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if ("" == name || null == name) {
            return false;
        }
        if ("" == price || null == price) {
            return false;
        }
        if ("" == img || null == img) {
            return false;
        }
        return true;
    }
}
