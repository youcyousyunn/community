package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.cmmbo.dao.QuestionDao;
import com.ycs.community.cmmbo.domain.dto.*;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.service.QuestionService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private QuestionDao questionDao;

	@Override
    @Transactional (rollbackFor = {CustomizeBusinessException.class})
	public boolean askQuestion(QuestionRequestDto request) throws CustomizeBusinessException {
		QuestionPo questionPo = BeanUtil.trans2Entity(request, QuestionPo.class);
		questionPo.setCreTm(new Date().getTime());
		int result = questionDao.askQuestion(questionPo);
		if (result == 1) {
			return true;
		}
        throw new CustomizeBusinessException(HiMsgCdConstants.ASK_QUESTION_FAIL, "提问失败");
	}

    @Override
    public boolean delQuestion(Long id) {
	    int result = questionDao.delQuestion(id);
	    if (result == 1) {
	        return true;
        }
        return false;
    }

    @Override
    public boolean updQuestion(QuestionRequestDto request) {
        QuestionPo questionPo = BeanUtil.trans2Entity(request, QuestionPo.class);
        questionPo.setCreTm(new Date().getTime());
        int result = questionDao.updQuestion(request);
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
	@Transactional(readOnly = true)
	public QryQuestionPageResponseDto qryQuestionPage(QryQuestionPageRequestDto request) {
		Map<String, Object> paramMap = new HashMap<>();
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
