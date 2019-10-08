package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.TopicResponseDto;
import com.ycs.community.cmmbo.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	@Autowired
	private IndexService indexService;

	/**
	 * 查询话题列表
	 * @return
	 */
	@GetMapping("/qryTopicList")
	public TopicResponseDto qryTopicList() {
		TopicResponseDto responseDto = new TopicResponseDto();
		responseDto = indexService.qryTopicList();
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

}
