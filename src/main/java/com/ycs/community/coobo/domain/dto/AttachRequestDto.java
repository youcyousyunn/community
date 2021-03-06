package com.ycs.community.coobo.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.coobo.domain.po.AttachPo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class AttachRequestDto extends BaseRequestDto {
    private Long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == String.valueOf(id) || StringUtils.isEmpty(String.valueOf(id))) {
            return false;
        }
        if (null == name || StringUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }

    /**
     * 复制 Bean
     * @param source
     */
    public void copyBean(AttachPo source) {
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
