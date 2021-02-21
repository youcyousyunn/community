package com.ycs.community.activiti.domain.dto;

import com.ycs.community.activiti.domain.po.FlowDef;
import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class QryActivitiFlowResponseDto extends BaseResponseDto {
    private List<FlowDef> data;
}
