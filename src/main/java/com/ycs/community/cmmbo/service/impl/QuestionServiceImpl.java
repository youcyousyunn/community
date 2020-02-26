package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.cmmbo.dao.AnswerDao;
import com.ycs.community.cmmbo.dao.CommentDao;
import com.ycs.community.cmmbo.dao.QuestionDao;
import com.ycs.community.cmmbo.dao.TagDao;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageRequestDto;
import com.ycs.community.cmmbo.domain.dto.QryQuestionPageResponseDto;
import com.ycs.community.cmmbo.domain.dto.QuestionRequestDto;
import com.ycs.community.cmmbo.domain.dto.QuestionResponseDto;
import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.cmmbo.domain.po.TagPo;
import com.ycs.community.cmmbo.service.QuestionService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
	private Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private TagDao tagDao;
	@Value("${lucene.index.path}")
	private String indexPath;

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
	@Transactional(rollbackFor = CustomizeBusinessException.class)
    public boolean updQuestion(QuestionRequestDto request) {
        QuestionPo questionPo = BeanUtil.trans2Entity(request, QuestionPo.class);
        questionPo.setUpdTm(new Date().getTime());
        int result = questionDao.updQuestion(questionPo);
        if (result == 1) {
//			addIndex(questionPo);
        }  else {
        	throw new CustomizeBusinessException(HiMsgCdConstants.UPD_QUESTION_FAIL, "更新问题失败");
		}
		return true;
    }

    @Override
	public QuestionResponseDto qryQuestion(Long id) {
		QuestionResponseDto response = new QuestionResponseDto();
		QuestionPo questionPo = questionDao.qryQuestion(id);
		if (StringUtils.isEmpty(questionPo)) {
			throw new CustomizeBusinessException(HiMsgCdConstants.QRY_QUESTION_FAIL, "问题不存在或已被删除");
		}
		// 查询问题标签
		if (!StringUtils.isEmpty(questionPo.getTag())) {
			List<String> ids = Arrays.asList(questionPo.getTag().split(","));
			List<TagPo> tagPoList = tagDao.qryTagListByIds(ids);
			if (!CollectionUtils.isEmpty(tagPoList)) {
				questionPo.setTagList(tagPoList);
			}
			response.setData(questionPo);
		}

		return response;
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

		// 查询问题标签
		if (!CollectionUtils.isEmpty(data)) {
			data.forEach(questionPo -> {
				List<String> ids = Arrays.asList(questionPo.getTag().split(","));
				List<TagPo> tagPoList = tagDao.qryTagListByIds(ids);
				if (!CollectionUtils.isEmpty(tagPoList)) {
					questionPo.setTagList(tagPoList);
				}
			});
		}

		// 组装分页信息
		QryQuestionPageResponseDto response = new QryQuestionPageResponseDto();
		if (!CollectionUtils.isEmpty(data)) {
			response.setData(data);
			response.setTotal(totalCount);
            return response;
        }
		return response;
	}

	/**
	 * 添加索引
	 * @param questionPo
	 */
	private void addIndex(QuestionPo questionPo) {
		// 建立索引
		IndexWriter indexWriter = null;
		try {
			Directory dir = null;
			dir = FSDirectory.open(Paths.get(indexPath));
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
			indexWriter = new IndexWriter(dir, indexWriterConfig);
			Document doc = new Document();
			doc.add(new StringField("id", questionPo.getId().toString(), Field.Store.YES));
			doc.add(new TextField("description", questionPo.getDescription(), Field.Store.YES));
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			logger.error("给问题添加索引失败: {}", e.getMessage());
		} finally {
			try {
				indexWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据索引获取数据
	 * @param str
	 * @return
	 */
	private List<QuestionPo> qryObjectByIndex(String str) {
		List<QuestionPo> result = new ArrayList<>();
		IndexReader indexReader = null;
		Analyzer analyzer = new StandardAnalyzer();
		analyzer.setVersion(Version.LUCENE_6_1_0);
		try {
			QueryParser queryParser = new QueryParser("id", analyzer);
			Query query = queryParser.parse(str);
			DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			indexReader = directoryReader;
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TopDocs topDocs = indexSearcher.search(query, 10);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < scoreDocs.length; i++) {
				ScoreDoc scoreDoc = scoreDocs[i];
				Document doc = indexSearcher.doc(scoreDoc.doc);
				QuestionPo questionPo = new QuestionPo();
				questionPo.setId(Long.parseLong(doc.get("id")));
				questionPo.setDescription(doc.get("description"));
				result.add(questionPo);
			}
		} catch (Exception e) {
			logger.error("根据索引获取数据失败: {}", e.getMessage());
		} finally {
			try {
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			analyzer.close();
		}

		return result;
	}
}
