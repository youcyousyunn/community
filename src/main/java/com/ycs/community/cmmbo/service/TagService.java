package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.TagResponseDto;

public interface TagService {
    TagResponseDto qryTagList(long pid);
    TagResponseDto qryMostStarTagList();
}
