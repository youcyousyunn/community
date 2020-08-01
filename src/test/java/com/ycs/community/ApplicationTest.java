package com.ycs.community;

import cn.hutool.json.JSONUtil;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.coobo.domain.po.JDDocumentPo;
import com.ycs.community.coobo.utils.HtmlParseUtil;
import com.ycs.community.sysbo.activemq.producer.QueueProducer;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	protected final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

	@Autowired
	private QueueProducer queueProducer;
	@Autowired
	@Qualifier("restHighLevelClient")
	private RestHighLevelClient client;

	@Test
	public void testSend() {
		String msg = "订单号： " + UUID.randomUUID().toString();
		queueProducer.sendMsg(msg);
		logger.info("恭喜，发送消息成功！！！\n{}", msg);
	}

	@Test
	public void createIndex() throws IOException {
		// 创建索引请求
		CreateIndexRequest request = new CreateIndexRequest("yang");
		// 执行请求
		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
		boolean isAcknowledged = response.isAcknowledged();
		logger.info("创建索引是否成功：{}", isAcknowledged);
	}

	@Test
	public void isExistIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest("es");
		boolean isExist = client.indices().exists(request, RequestOptions.DEFAULT);
		logger.info("索引是否存在：{}", isExist);
	}

	@Test
	public void delIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest("yang");
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		boolean isDeleted = response.isAcknowledged();
		logger.info("是否删除成功：{}", isDeleted);
	}

	@Test
	public void addDocument() throws IOException {
		// 创建文档对象
		QuestionPo question = new QuestionPo();
		question.setTitle("测试问题-001");
		question.setDescription("这是问题001描述信息");
		// 创建请求
		IndexRequest request = new IndexRequest("es");
		request.id("001");
		// 设置超时
		request.timeout(TimeValue.timeValueSeconds(20));
		request.source(JSONUtil.toJsonStr(question), XContentType.JSON);
		// 客户端发送请求, 获取响应
		IndexResponse response = client.index(request, RequestOptions.DEFAULT);
		RestStatus status = response.status();
		logger.info("添加文档状态：{}", status);
	}

	@Test
	public void isExistDocument() throws IOException {
		GetRequest request = new GetRequest("es", "001");
		request.fetchSourceContext(new FetchSourceContext(false));
		boolean isExist = client.exists(request, RequestOptions.DEFAULT);
		logger.info("文档是否存在：{}", isExist);
	}

	@Test
	public void getDocument() throws IOException {
		GetRequest request = new GetRequest("es", "001");
		GetResponse response = client.get(request, RequestOptions.DEFAULT);
		Map<String, Object> result = response.getSourceAsMap();
		logger.info("获取到文档中所有字段如下：{}", result);
	}

	@Test
	public void updDocument() throws IOException {
		UpdateRequest request = new UpdateRequest("es", "001");
		request.timeout(TimeValue.timeValueSeconds(20));
		QuestionPo question = new QuestionPo();
		question.setTitle("测试问题-001");
		question.setDescription("这是问题001描述信息-有更新");
		request.doc(JSONUtil.toJsonStr(question), XContentType.JSON);
		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
		RestStatus status = response.status();
		logger.info("更新文档结果：{}", status);
	}

	@Test
	public void delDocument() throws IOException {
		DeleteRequest request = new DeleteRequest("es", "001");
		request.timeout(TimeValue.timeValueSeconds(20));
		DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
		RestStatus status = response.status();
		logger.info("删除文档结果：{}", status);
	}

	@Test
	public void addBulkDocument() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.timeout(TimeValue.timeValueSeconds(60));

		List<QuestionPo> questionList = new ArrayList<>();
		for (int i=0; i<3; i++) {
			QuestionPo question = new QuestionPo();
			question.setTitle("问题标题" + i);
			question.setDescription("问题描述" + i);
			questionList.add(question);
		}

		for (int j=0; j<questionList.size(); j++) {
			bulkRequest.add(new IndexRequest("test")
					.id(j + "")
					.source(JSONUtil.toJsonStr(questionList.get(j)), XContentType.JSON));
		}

		BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		boolean hasFailures = response.hasFailures();
		logger.info("批量插入是否失败：{}", hasFailures);
	}

	@Test
	public void addBulkJDDocument() throws IOException {
		String uri = "https://search.jd.com/Search?keyword=java&enc=utf-8";
		List<JDDocumentPo> sources = HtmlParseUtil.parseHtml(uri);
		// 批量插入请求
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.timeout(TimeValue.timeValueSeconds(60));

		for (JDDocumentPo item : sources) {
			bulkRequest.add(
				new IndexRequest("jd")
				.id(UUID.randomUUID().toString())
				.source(JSONUtil.toJsonStr(item), XContentType.JSON)
			);
		}

		BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		boolean hasFailures = response.hasFailures();
		logger.info("批量插入JD数据是否失败：{}", hasFailures);
	}

	@Test
	public void search() throws IOException {
		SearchRequest request = new SearchRequest("es");
		// 构建搜索条件
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.highlighter(); // 高亮显示
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "测试标题");
		searchSourceBuilder.query(termQueryBuilder);
		// 设置超时
		searchSourceBuilder.timeout(TimeValue.timeValueSeconds(20));

		SearchResponse response = client.search(request, RequestOptions.DEFAULT);
		for (SearchHit item : response.getHits().getHits()) {
			Map<String, Object> resutlMap = item.getSourceAsMap();
			logger.info("获取搜索结果：　{}", resutlMap);
		}
	}
}
