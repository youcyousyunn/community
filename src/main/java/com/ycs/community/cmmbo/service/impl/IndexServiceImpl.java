package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.cmmbo.dao.IndexDao;
import com.ycs.community.cmmbo.domain.dto.TopicResponseDto;
import com.ycs.community.cmmbo.domain.po.TopicPo;
import com.ycs.community.cmmbo.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {
	@Autowired
	private IndexDao indexDao;

	@Override
	public TopicResponseDto qryTopicList() {
        TopicResponseDto response = new TopicResponseDto();
		List<TopicPo> topicList = indexDao.qryTopicList();
        response.setData(topicList);
		return response;
	}


}
