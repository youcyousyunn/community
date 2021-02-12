package com.ycs.community.activiti.domain.dto;

import com.ycs.community.activiti.domain.po.TaskPo;
import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class QryActivitiTaskPageResponseDto extends BaseResponseDto {
    private List<TaskPo> data;
    private Integer total;
}
