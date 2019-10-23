package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseResponseDto;
import com.ycs.community.sysbo.domain.po.MenuPo;
import lombok.Data;

import java.util.List;

@Data
public class MenuResponseDto extends BaseResponseDto {
    private List<MenuPo> data;
}
