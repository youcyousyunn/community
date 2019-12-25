package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.cmmbo.dao.TagDao;
import com.ycs.community.cmmbo.domain.dto.TagResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;
import com.ycs.community.cmmbo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public TagResponseDto qryTagList(long pid) {
        TagResponseDto response = new TagResponseDto();
        List<TagPo> data = tagDao.qryTagList(pid);
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
        }
        return response;
    }

    @Override
    public TagResponseDto qryMostStarTagList() {
        TagResponseDto response = new TagResponseDto();
        List<TagPo> data = tagDao.qryMostStarTagList();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
        }
        return response;
    }
}