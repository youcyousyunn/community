package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.cmmbo.dao.AnswerDao;
import com.ycs.community.cmmbo.dao.CommentDao;
import com.ycs.community.cmmbo.dao.QuestionDao;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageRequestDto;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageResponseDto;
import com.ycs.community.cmmbo.domain.dto.QuestionRequestDto;
import com.ycs.community.cmmbo.domain.dto.QuestionResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.service.QuestionService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private AnswerDao answerDao;

	@Override
    @Transactional (rollbackFor = {CustomizeBusinessException.class})
	public boolean askQuestion(QuestionRequestDto request) throws CustomizeBusinessException {
		QuestionPo questionPo = BeanUtil.trans2Entity(request, QuestionPo.class);
		if (StringUtils.isEmpty(questionPo.getCreator())) {
			questionPo.setCreator(SecurityUtil.getAccountId());
		}
		questionPo.setCreTm(new Date().getTime());
		int result = questionDao.askQuestion(questionPo);
		if (result == 1) {
			return true;
		}
        throw new CustomizeBusinessException(HiMsgCdConstants.ASK_QUESTION_FAIL, "提问失败");
	}

    @Override
	@Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delQuestion(Long id) {
		// 删除问题前删除问题的评论
		if (commentDao.delCommentsByQuestionId(id) < 0) {
			throw new CustomizeBusinessException(HiMsgCdConstants.DEL_QUESTION_COMMENT_FAIL, "删除问题评论失败");
		}

		// 删除问题前删除回答
		List<AnswerPo> answerPoList = answerDao.qryAnswersByQuestionId(id);
		if (!CollectionUtils.isEmpty(answerPoList)) {
			// 删除问题回答前删除评论
			List<Long> answerIds = answerPoList.stream().map(AnswerPo :: getId).collect(Collectors.toList());
			if (commentDao.delCommentsByAnswerIds(answerIds) < 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ANSWER_COMMENT_FAIL, "删除回答评论失败");
			}
			if (answerDao.delAnswersByQuestionId(id) < 1) {
				throw new CustomizeBusinessException(HiMsgCdConstants.DEL_QUESTION_ANSWER_FAIL, "删除问题回答失败");
			}
		}

		// 删除问题
	    int result = questionDao.delQuestion(id);
	    if (result == 1) {
	        return true;
        }
        return false;
    }

    @Override
    public boolean updQuestion(QuestionRequestDto request) {
        QuestionPo questionPo = BeanUtil.trans2Entity(request, QuestionPo.class);
        questionPo.setUpdTm(new Date().getTime());
        int result = questionDao.updQuestion(questionPo);
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override
	public QuestionResponseDto qryQuestion(Long id) {
		QuestionResponseDto result = new QuestionResponseDto();
		QuestionPo questionPo = questionDao.qryQuestion(id);
		if (null != questionPo) {
			result.setData(questionPo);
		}
		return result;
	}

	@Override
	public boolean increaseView(Long id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("viewCount", 1);
		int result = questionDao.increaseView(paramMap);
		if (result != 1) {
			throw new CustomizeBusinessException(HiMsgCdConstants.INCREASE_QUESTION_VIEW_COUNT_FAIL, "问题浏览数累加失败");
		}
		return true;
	}

	@Override
	public QryQuestionPageResponseDto qryQuestionPage(QryQuestionPageRequestDto request) {
		Map<String, Object> paramMap = new HashMap<>();
		if (!StringUtils.isEmpty(request.getStartTime())) {
			paramMap.put("startTime", request.getStartTime().getTime());
		}
		if (!StringUtils.isEmpty(request.getEndTime())) {
			paramMap.put("endTime", request.getEndTime().getTime());
		}
		paramMap.put("type", request.getType());
		paramMap.put("tag", request.getTag());
		paramMap.put("name", request.getName());
		// 查询总条数
		int totalCount = questionDao.qryQuestionCount(paramMap);
		// 计算分页信息
		PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
		// 分页查询
		paramMap.put("startRow", PageUtil.getStartRow());
		paramMap.put("offset", PageUtil.getPageSize());
		List<QuestionPo> data = questionDao.qryQuestionPage(paramMap);
		// 组装分页信息
		QryQuestionPageResponseDto response = new QryQuestionPageResponseDto();
		if (!CollectionUtils.isEmpty(data)) {
			response.setData(data);
			response.setTotal(totalCount);
            return response;
        }
		return response;
	}
}
