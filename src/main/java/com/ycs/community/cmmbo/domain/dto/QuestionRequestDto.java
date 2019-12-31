package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class QuestionRequestDto extends BaseRequestDto {
    private Long id;
    private String title;
    private String description;
    private long type;
    private String tag;
    private Long creator;
    private int likeCount;
    private int commentCount;
    private int viewCount;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        if (StringUtils.isEmpty(title)) {
            return false;
        }
        if (StringUtils.isEmpty(description)) {
            return false;
        }
        if (StringUtils.isEmpty(tag)) {
            return false;
        }
        return true;
    }
}
