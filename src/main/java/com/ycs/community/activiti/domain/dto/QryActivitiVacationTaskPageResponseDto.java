package com.ycs.community.activiti.domain.dto;

import com.ycs.community.activiti.domain.po.VacationTaskPo;
import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class QryActivitiVacationTaskPageResponseDto extends BaseResponseDto {
    private List<VacationTaskPo> data;
    private Integer total;
}
