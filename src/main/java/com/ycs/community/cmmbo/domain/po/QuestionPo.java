package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;

@Data
public class QuestionPo extends BasePo {
    private Long id;
    private String title;
    private String description;
    private Long type;
    private String tag;
    private List<TagPo> tagList;
    private Long creator;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private String userName;
    private String nickname;
    private String avatar;
}
