package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class QuestionPo extends BasePo {
    private Long id;
    private String title;
    private String description;
    private Long type;
    private String tag;
    private Long creator;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private String userName;
    private String nickname;
    private String avatar;
}
