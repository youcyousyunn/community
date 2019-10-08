package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class CommentPo extends BasePo {
    private Long id;
    private Long parentId;
    private int commentType;
    private int commentator;
    private int likeCount;
    private String comment;
    private int commentCount;
    private String userName;
    private String avatarUrl;
}
