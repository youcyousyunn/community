package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.MenuRequestDto;
import com.ycs.community.sysbo.domain.dto.MenuResponseDto;

public interface MenuService {
    MenuResponseDto qryMenu(MenuRequestDto request);
}
