package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.MenuPo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class JobRequestDto extends BaseRequestDto {
    private Long id;
    private String name;
    private DeptPo dept;
    private int sort;
    private Boolean enabled;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        if (StringUtils.isEmpty(dept)) {
            return false;
        }
        if (StringUtils.isEmpty(enabled)) {
            return false;
        }
        return true;
    }
}
