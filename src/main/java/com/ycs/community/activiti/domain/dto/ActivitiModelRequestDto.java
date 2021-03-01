package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ActivitiModelRequestDto extends BaseRequestDto {
    private String id;
    private String flowId;
    private String key;
    private String deploymentId;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(flowId)) {
            return false;
        }
        return true;
    }
}
