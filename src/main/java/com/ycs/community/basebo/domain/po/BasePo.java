package com.ycs.community.basebo.domain.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePo implements Serializable {
    private static final long serialVersionUID = 1L;
    private long creDt;
    private long creTm;
    private long updDt;
    private long updTm;
}
