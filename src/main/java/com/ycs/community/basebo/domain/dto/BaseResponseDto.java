package com.ycs.community.basebo.domain.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class BaseResponseDto {
    private String rspCode;
    private String rspInf;
    private String rspTm;

    public BaseResponseDto(){}

    /**
     * 构造函数
     * @param rspCode
     */
    public BaseResponseDto(String rspCode) {
        this.rspCode = rspCode;
    }

    /**
     * 构造函数
     * @param rspCode
     * @param rspInf
     */
    public BaseResponseDto(String rspCode, String rspInf) {
        this.rspCode = rspCode;
        this.rspInf = rspInf;
    }

    public String getRspTm() {
        if (null == this.rspTm) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.rspTm = sdf.format(new Date());
        }
        return rspTm;
    }

    public void setRspTm(String rspTm) {
        this.rspTm = rspTm;
    }
}
