package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class QuartzJobPo extends BasePo {
    public static final String JOB_KEY = "JOB_KEY";
    private Long id;
    private String name;
    private String cron;
    private String beanNm;
    private String methodNm;
    private String params;
    private Boolean isPause; // 状态: 1暂停、0启用
    private String remark;
}
