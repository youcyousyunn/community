package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;

@Data
public class DeptPo extends BasePo {
    private Long id;
    private String name;
    private Long pid;
    private boolean enabled;
    private List<DeptPo> children;

    public String getLabel() {
        return name;
    }
}