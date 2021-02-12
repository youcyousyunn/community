package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import lombok.Data;
import org.activiti.engine.repository.Model;

import java.util.List;
@Data
public class QryActivitiModelPageResponseDto extends BaseResponseDto {
    private List<Model> data;
    private Integer total;
}
