package com.ycs.community.cmmbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class AnswerRequestDto extends BaseRequestDto {
    private Long id;
    private Long questionId;
    private String answer;
    private int commentCount;
    private int likeCount;
    private Long answerer;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == String.valueOf(questionId) || StringUtils.isEmpty(String.valueOf(questionId))) {
            return false;
        }
        if (null == answer || StringUtils.isEmpty(answer)) {
            return false;
        }
        if (null == String.valueOf(answerer) || StringUtils.isEmpty(String.valueOf(answerer))) {
            return false;
        }
        return true;
    }
}
