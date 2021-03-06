package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.AnswerRequestDto;
import com.ycs.community.cmmbo.domain.dto.AnswerResponseDto;
import com.ycs.community.cmmbo.domain.dto.CommentResponseDto;
import com.ycs.community.cmmbo.service.AnswerService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "社区管理: 回答管理")
public class AnswerController {
	@Autowired
	private AnswerService answerService;

	/**
	 * 根据问题id查询回答
	 * @param questionId
	 * @return
	 */
	@GetMapping("/answer/{questionId}")
	@AnonymousAccess
	public AnswerResponseDto qryAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
		// 接口请求报文检查
		if (questionId.equals(null)) {
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		AnswerResponseDto responseDto = new AnswerResponseDto();
		responseDto = answerService.qryAnswersByQuestionId(questionId);
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}

	/**
	 * 回答问题
	 * @param request
	 * @return
	 * @throws CustomizeBusinessException
	 */
	@PostMapping("/answer")
	@AnonymousAccess
	public AnswerResponseDto answerQuestion(@RequestBody AnswerRequestDto request) throws CustomizeBusinessException {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		AnswerResponseDto responseDto = new AnswerResponseDto();
		try {
			answerService.answerQuestion(request);
		} catch (CustomizeBusinessException e) {
			responseDto.setRspCode(e.getCode());
			return responseDto;
		}
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}
}
