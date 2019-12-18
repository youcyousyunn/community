package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.TopicResponseDto;
import com.ycs.community.cmmbo.service.IndexService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "社区管理: 话题管理")
public class IndexController {
	@Autowired
	private IndexService indexService;

	/**
	 * 查询话题列表
	 * @return
	 */
	@GetMapping("/qryTopicList")
	@AnonymousAccess
	public TopicResponseDto qryTopicList() {
		TopicResponseDto responseDto = new TopicResponseDto();
		responseDto = indexService.qryTopicList();
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

}
