package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class CommentRequestDto extends BaseRequestDto {
    private Long id;
    private Long parentId;
    private int commentType; // 1,问题  2,答案
    private int commentator;
    private int likeCount;
    private String comment;
    private int commentCount;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == String.valueOf(commentator) || StringUtils.isEmpty(String.valueOf(commentator))) {
            return false;
        }
        if (null == comment || StringUtils.isEmpty(comment)) {
            return false;
        }
        return true;
    }
}
