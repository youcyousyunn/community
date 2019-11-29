package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.sysbo.domain.po.MenuMetaPo;
import com.ycs.community.sysbo.domain.po.MenuPo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class MenuRequestDto extends BaseRequestDto {
    private Long id;
    private Long pid;
    private String name;
    private String path;
    private String component;
    private String redirect;
    private boolean alwaysShow;
    private boolean iFrame;
    private boolean hidden;
    private boolean cache;
    private String icon;
    private String permission;
    private int type;
    private List<MenuPo> children;
    private MenuMetaPo meta;
    private int sort;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(pid)) {
            return false;
        }
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
}
