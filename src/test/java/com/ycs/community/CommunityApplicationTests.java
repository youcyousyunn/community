package com.ycs.community;

import com.ycs.community.sysbo.AsyncTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityApplicationTests {

	protected final Logger logger = LoggerFactory.getLogger(CommunityApplicationTests.class);

	@Autowired
	private AsyncTask asyncTask;

	@Test
	public void AsyncTaskTest() throws InterruptedException, ExecutionException {
		for (int i = 0; i < 100; i++) {
			asyncTask.doTask1(i);
		}
		logger.info("所有任务执行成功.");
	}
}
