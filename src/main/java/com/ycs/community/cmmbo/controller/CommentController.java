package com.ycs.community.cmmbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.*;
import com.ycs.community.cmmbo.service.CommentService;
import com.ycs.community.cmmbo.service.QuestionService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
	@Autowired
	private CommentService commentService;

    /**
     * 根据问题id查询评论
     * @param parentId
     * @param commentType
     * @return
     */
    @GetMapping("/comment/{parentId}")
    public CommentResponseDto qryComment(@PathVariable("parentId") Long parentId, @RequestParam(value = "commentType") int commentType) {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto = commentService.qryComment(parentId, commentType);
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
