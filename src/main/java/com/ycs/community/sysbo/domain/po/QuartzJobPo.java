package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class QuartzJobPo extends BasePo {
    public static final String JOB_KEY = "JOB_KEY";
    private Long id;
    private String name;
    private String beanNm;
    private String methodNm;
    private String params;
    private String cron;
    private Boolean isPause;
    private String remark;
}
