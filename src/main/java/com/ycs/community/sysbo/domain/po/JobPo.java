package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class JobPo extends BasePo {
    private Long id;
    private String name;
    private DeptPo dept;
    private Long deptId;
    private String deptName;
    private Long deptPid;
    private String deptSuperiorName;
    private int sort;
    private Boolean enabled;
}
