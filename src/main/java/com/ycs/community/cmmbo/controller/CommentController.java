package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.CommentRequestDto;
import com.ycs.community.cmmbo.domain.dto.CommentResponseDto;
import com.ycs.community.cmmbo.service.CommentService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "社区管理: 评论管理")
public class CommentController {
	@Autowired
	private CommentService commentService;

    /**
     * 根据问题id查询评论
     * @param questionId
     * @return
     */
    @GetMapping("/comment/{questionId}")
	@AnonymousAccess
    public CommentResponseDto qryCommentsByQuestionId(@PathVariable("questionId") Long questionId) {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto = commentService.qryCommentsByQuestionId(questionId);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 评论问题|答案
	 * @param request
     * @return
     * @throws CustomizeBusinessException
	 */
	@PostMapping("/comment")
	@AnonymousAccess
	public CommentResponseDto commentQuestionOrAnswer(@RequestBody CommentRequestDto request) throws CustomizeBusinessException {
		// 接口请求报文检查
		if (!request.checkRequestDto()) {
			BizLogger.info("接口请求报文异常" + request.toString());
			throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
		}
		CommentResponseDto responseDto = new CommentResponseDto();
		try {
			commentService.commentQuestionOrAnswer(request);
		} catch (CustomizeBusinessException e) {
			responseDto.setRspCode(e.getCode());
			return responseDto;
		}
		responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
		return responseDto;
	}
}
