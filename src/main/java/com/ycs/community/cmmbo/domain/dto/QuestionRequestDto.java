package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class QuestionRequestDto extends BaseRequestDto {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private int creator;
    private int likeCount;
    private int commentCount;
    private int viewCount;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == title || StringUtils.isEmpty(title)) {
            return false;
        }
        if (null == description || StringUtils.isEmpty(description)) {
            return false;
        }
        return true;
    }
}
