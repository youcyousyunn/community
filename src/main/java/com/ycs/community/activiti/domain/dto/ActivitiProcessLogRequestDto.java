package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ActivitiProcessLogRequestDto extends BaseRequestDto {
    private Long processId;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(processId)) {
            return false;
        }
        return true;
    }
}
