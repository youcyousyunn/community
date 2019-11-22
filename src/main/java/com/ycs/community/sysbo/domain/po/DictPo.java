package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;

@Data
public class DictPo extends BasePo {
    private Long id;
    private Long dictId;
    private String name;
    private String remark;
    private String label;
    private String value;
    private int sort;
    private List<DictPo> dictDetails;
}