package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class AnswerPo extends BasePo {
    private Long id;
    private Long questionId;
    private String answer;
    private int commentCount;
    private int likeCount;
    private Long answerer;
    private String userName;
    private String avatar;
}
