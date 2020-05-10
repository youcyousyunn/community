package com.ycs.community;

import com.ycs.community.sysbo.activemq.producer.QueueProducer;
import com.ycs.community.sysbo.domain.po.UserPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	protected final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

	@Autowired
	private QueueProducer queueProducer;
//	@Autowired
//	private JestClient jestClient;

	@Test
	public void testSearch() {
		String filter = "{\n" +
				"	\"query\" : {\n" +
				"		\"match\" : {\n" +
				"			\"usrNm\" : \"张三丰\n" +
				"		|\n" +
				"	}\n" +
				"}";
//		Search search = new Search.builder(filter).addIndex("eladmin")
//				.addType("user").build();
//		jestClient.execute(search);
	}

	@Test
	public void testSend() {
		String msg = "订单号： " + UUID.randomUUID().toString();
		queueProducer.sendMsg(msg);
		logger.info("恭喜，发送消息成功！！！\n{}", msg);
	}
}
