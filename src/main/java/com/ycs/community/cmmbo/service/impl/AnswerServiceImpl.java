package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.cmmbo.dao.AnswerDao;
import com.ycs.community.cmmbo.dao.CommentDao;
import com.ycs.community.cmmbo.dao.QuestionDao;
import com.ycs.community.cmmbo.domain.dto.AnswerRequestDto;
import com.ycs.community.cmmbo.domain.dto.AnswerResponseDto;
import com.ycs.community.cmmbo.domain.dto.CommentRequestDto;
import com.ycs.community.cmmbo.domain.dto.CommentResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.service.AnswerService;
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
public class AnswerServiceImpl implements AnswerService {
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private QuestionDao questionDao;

	@Override
	public AnswerResponseDto qryAnswersByQuestionId(Long questionId) {
		AnswerResponseDto response = new AnswerResponseDto();
		List<AnswerPo> data = answerDao.qryAnswersByQuestionId(questionId);
		if (!CollectionUtils.isEmpty(data)) {
			response.setData(data);
			return response;
		}
		return response;
	}

	@Override
	@Transactional (rollbackFor = {CustomizeBusinessException.class})
	public boolean answerQuestion(AnswerRequestDto request) {
		// 评论之前检测问题是否存在
		boolean isExist = isExistQuestion(request.getQuestionId());
		if (!isExist) {
			throw new CustomizeBusinessException(HiMsgCdConstants.QUESTION_NOT_EXIST, "问题已不存在");
		}
		AnswerPo answerPo = BeanUtil.trans2Entity(request, AnswerPo.class);
		answerPo.setCreTm(new Date().getTime());
		int result = answerDao.answerQuestion(answerPo);
		if (result != 1) {
			throw new CustomizeBusinessException(HiMsgCdConstants.ANSWER_QUESTION_FAIL, "回答问题失败");
		}
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
