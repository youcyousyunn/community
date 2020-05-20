package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.TagRequestDto;
import com.ycs.community.cmmbo.domain.dto.TagResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;
import com.ycs.community.cmmbo.service.TagService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationTypeEnum;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "社区管理: 标签管理")
public class TagController {
	@Autowired
	private TagService tagService;

	/**
	 * 查询标签树
	 * @param request
	 * @return
	 */
	@GetMapping("/tag/tree")
	public TagResponseDto qryTagTree(TagRequestDto request) {
		TagResponseDto responseDto = new TagResponseDto();
		responseDto = tagService.qryTagTree(request);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 查询标签类别树
	 * @param pid
	 * @return
	 */
	@GetMapping("/tab/tree/{pid}")
	public ResponseEntity qryTagTabTree(@PathVariable("pid") Long pid) {
		// 接口请求报文检查
		if (null == pid || StringUtils.isEmpty(pid)) {
			BizLogger.info("接口请求报文异常" + pid);
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		List<Map<String, Object>> response = tagService.qryTagTabTree(tagService.qryTagListByPid(pid));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 添加标签
	 * @param request
	 * @return
	 */
	@PostMapping("/tag")
	public TagResponseDto addTag(@RequestBody TagRequestDto request) {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			BizLogger.info("接口请求报文异常" + request.toString());
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		TagResponseDto responseDto  = new TagResponseDto();
		tagService.addTag(request);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 根据ID删除标签
	 * @param id
	 * @return
	 */
	@DeleteMapping("/tag/{id}")
	@OperationLog(title = "根据ID删除标签", action = OperationTypeEnum.GET, isSave = true, channel = "web")
	public TagResponseDto delTag(@PathVariable("id") Long id) {
		TagResponseDto responseDto = new TagResponseDto();
		tagService.delTag(id);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 更新标签
	 * @param request
	 * @return
	 */
	@PutMapping("/tag")
	public TagResponseDto updTag(@RequestBody TagRequestDto request) {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			BizLogger.info("接口请求报文异常" + request.toString());
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		TagResponseDto responseDto  = new TagResponseDto();
		tagService.updTag(request);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

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
	 * 根据标签类别查询标签列表
	 * @param pid
	 * @return
	 */
	@GetMapping("/qryTagList/{pid}")
	@AnonymousAccess
	public TagResponseDto qryTagListByPid(@PathVariable("pid") long pid) {
		TagResponseDto responseDto = new TagResponseDto();
		List<TagPo> data = tagService.qryTagListByPid(pid);
		if (!CollectionUtils.isEmpty(data)) {
			responseDto.setData(data);
		}
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

}
