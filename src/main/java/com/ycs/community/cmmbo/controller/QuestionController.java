package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageRequestDto;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageResponseDto;
import com.ycs.community.cmmbo.domain.dto.QuestionRequestDto;
import com.ycs.community.cmmbo.domain.dto.QuestionResponseDto;
import com.ycs.community.cmmbo.service.QuestionService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "社区管理: 问题管理")
public class QuestionController {
	@Autowired
	private QuestionService questionService;

	/**
	 * 提问
	 * @param request
	 * @return
	 */
	@PostMapping("/question")
	@AnonymousAccess
	public QuestionResponseDto askQuestion(@RequestBody QuestionRequestDto request) throws CustomizeBusinessException {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			BizLogger.info("接口请求报文异常" + request.toString());
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		QuestionResponseDto responseDto = new QuestionResponseDto();
		try {
			questionService.askQuestion(request);
		} catch (CustomizeBusinessException e) {
			responseDto.setRspCode(e.getCode());
			return responseDto;
		}
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 根据id删除单个问题
	 * @param id
	 * @return
	 */
	@DeleteMapping("/question/{id}")
	@AnonymousAccess
	public QuestionResponseDto delQuestion(@PathVariable("id") Long id) {
		QuestionResponseDto responseDto = new QuestionResponseDto();
		if (questionService.delQuestion(id)) {
			responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		}
		return responseDto;
	}

	/**
	 * 更新提问
	 * @param request
	 * @return
	 */
	@PutMapping("/question")
	@AnonymousAccess
	public QuestionResponseDto updQuestion(@RequestBody QuestionRequestDto request) {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			BizLogger.info("接口请求报文异常" + request.toString());
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		QuestionResponseDto responseDto = new QuestionResponseDto();
		if (questionService.updQuestion(request)) {
			responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		}
		return responseDto;
	}

	/**
	 * 根据id查询单个问题
	 * @param id
	 * @return
	 */
	@GetMapping("/question/{id}")
	@AnonymousAccess
	public QuestionResponseDto qryQuestion(@PathVariable("id") Long id) {
		QuestionResponseDto responseDto = new QuestionResponseDto();
		responseDto = questionService.qryQuestion(id);
		// 累加阅读数
		questionService.increaseView(id);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 分页查询提问列表
	 * @param request
	 * @return
	 */
	@GetMapping("/question/queryPage")
	@AnonymousAccess
	public QryQuestionPageResponseDto qryQuestionPage(QryQuestionPageRequestDto request) {
		QryQuestionPageResponseDto responsePageDto = new QryQuestionPageResponseDto();
		responsePageDto = questionService.qryQuestionPage(request);
		responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responsePageDto;
	}
}
