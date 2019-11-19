package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.sysbo.domain.po.MenuPo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class RoleRequestDto extends BaseRequestDto {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private List<MenuPo> menus;
    // temp variables
    private String dataScope;
    private int level;
    private List<Long> depts;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
}
