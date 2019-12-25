package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.cmmbo.dao.IndexDao;
import com.ycs.community.cmmbo.domain.dto.TopicResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;
import com.ycs.community.cmmbo.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {
	@Autowired
	private IndexDao indexDao;

	@Override
	public TopicResponseDto qryTopicList(long pid) {
        TopicResponseDto response = new TopicResponseDto();
		List<TagPo> topicList = indexDao.qryTopicList(pid);
        response.setData(topicList);
		return response;
	}


}
