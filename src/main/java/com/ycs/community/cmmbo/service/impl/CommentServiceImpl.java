package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.enums.CommentTypeEnum;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.cmmbo.dao.AnswerDao;
import com.ycs.community.cmmbo.dao.CommentDao;
import com.ycs.community.cmmbo.dao.QuestionDao;
import com.ycs.community.cmmbo.domain.dto.CommentRequestDto;
import com.ycs.community.cmmbo.domain.dto.CommentResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.service.CommentService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private AnswerDao answerDao;

	@Override
	public CommentResponseDto qryCommentsByQuestionId(Long questionId) {
		CommentResponseDto response = new CommentResponseDto();
		List<CommentPo> data = commentDao.qryCommentsByQuestionId(questionId);
		if (!CollectionUtils.isEmpty(data)) {
			response.setData(data);
			return response;
		}
		return response;
	}

	@Override
	@Transactional (rollbackFor = {CustomizeBusinessException.class})
	public boolean commentQuestionOrAnswer(CommentRequestDto request) {
		// 评论问题
		if (request.getCommentType() == CommentTypeEnum.QUESTION.getType()) {
			// 评论之前检测问题是否存在
			boolean isExist = isExistQuestion(request.getParentId());
			if (!isExist) {
				throw new CustomizeBusinessException(HiMsgCdConstants.QUESTION_NOT_EXIST, "问题已不存在");
			}
			CommentPo commentPo = BeanUtil.trans2Entity(request, CommentPo.class);
			commentPo.setCreTm(new Date().getTime());
			int result = commentDao.commentQuestionOrAnswer(commentPo);
			if (result != 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.COMMENT_QUESTION_FAIL, "评论问题失败");
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", request.getParentId());
			paramMap.put("commentCount", 1);
			result = commentDao.increaseComment(paramMap);
			if (result != 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.INCREASE_QUESTION_COMMENT_COUNT_FAIL, "问题评论数累加失败");
			}
		} else { // 评论答案
			// 评论之前检测答案是否存在
			AnswerPo answerPo = answerDao.qryAnswer(request.getParentId());
			if (StringUtils.isEmpty(answerPo)) {
				throw new CustomizeBusinessException(HiMsgCdConstants.ANSWER_NOT_EXIST, "答案已不存在");
			}

			// 评论之前检测问题是否存在
			boolean isExist = isExistQuestion(answerPo.getQuestionId());
			if (!isExist) {
				throw new CustomizeBusinessException(HiMsgCdConstants.QUESTION_NOT_EXIST, "问题已不存在");
			}
			CommentPo commentPo = BeanUtil.trans2Entity(request, CommentPo.class);
			commentPo.setCreTm(new Date().getTime());
			int result = commentDao.commentQuestionOrAnswer(commentPo);
			if (result != 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.COMMENT_ANSWER_FAIL, "评论答案失败");
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", request.getParentId());
			paramMap.put("commentCount", 1);
			result = answerDao.increaseAnswer(paramMap);
			if (result != 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.INCREASE_ANSWER_COMMENT_COUNT_FAIL, "答案评论数累加失败");
			}
		}

		//// todo 通知问题发起人
		return true;
	}

	/**
	 * 查询问题是否存在
	 * @param questionId
	 * @return
	 */
	private boolean isExistQuestion (Long questionId) {
		QuestionPo questionPo = questionDao.qryQuestion(questionId);
		if (StringUtils.isEmpty(questionPo)) {
			return false;
		}
		return true;
	}
}
