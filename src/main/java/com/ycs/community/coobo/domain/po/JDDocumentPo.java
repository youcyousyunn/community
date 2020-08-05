package com.ycs.community.coobo.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JDDocumentPo {
    private String id;
    private String name;
    private String hlName; // 高亮名称
    private String price;
    private String desc;
    private String img;
}
