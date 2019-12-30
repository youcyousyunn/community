package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.TagRequestDto;
import com.ycs.community.cmmbo.domain.dto.TagResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;

import java.util.List;
import java.util.Map;

public interface TagService {
    TagResponseDto qryTagTree(TagRequestDto request);
    List<Map<String, Object>> qryTagTabTree(List<TagPo> tags);
    List<TagPo> qryTagListByPid(long pid);
    TagResponseDto qryMostStarTagList();
    boolean addTag(TagRequestDto request);
    boolean delTag(Long id);
    boolean updTag(TagRequestDto request);
}
