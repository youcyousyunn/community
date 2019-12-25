package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.TagResponseDto;
import com.ycs.community.cmmbo.service.TagService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "社区管理: 标签管理")
public class TagController {
	@Autowired
	private TagService tagService;

	/**
	 * 查询最多星数标签列表
	 * @return
	 */
	@GetMapping("/qryMostStarTagList")
	@AnonymousAccess
	public TagResponseDto qryMostStarTagList() {
		TagResponseDto responseDto = new TagResponseDto();
		responseDto = tagService.qryMostStarTagList();
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 查询标签列表
	 * @param pid
	 * @return
	 */
	@GetMapping("/qryTagList/{pid}")
	@AnonymousAccess
	public TagResponseDto qryTagList(@PathVariable("pid") long pid) {
		TagResponseDto responseDto = new TagResponseDto();
		responseDto = tagService.qryTagList(pid);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

}
