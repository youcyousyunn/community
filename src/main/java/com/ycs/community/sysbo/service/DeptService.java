package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;

public interface DeptService {
    DeptResponseDto qryDeptTree(DeptRequestDto request);
}
