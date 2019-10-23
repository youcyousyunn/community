package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuMetaPo extends BasePo {
    private String title;
    private String icon;
    private boolean noCache;
}
