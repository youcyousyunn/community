package com.ycs.community.activiti.domain.dto;

import com.ycs.community.activiti.domain.po.ProcessLog;
import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiProcessLogResponseDto extends BaseResponseDto {
    List<ProcessLog> data;
}
