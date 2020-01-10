package com.ycs.community.sysbo.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.UUID;

@Component
public class TopicProducer {
    private Logger logger = LoggerFactory.getLogger(QueueProducer.class);
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;

    /**
     * 发送订阅消息
     * @param msg
     */
    public void sendTopicMsg(String msg) {
        jmsMessagingTemplate.convertAndSend(topic, msg);
        logger.info("发送" + msg + "成功！！");
    }

    /**
     * 间隔时间发送订阅消息
     */
    @Scheduled(fixedDelay = 5000)
    public void sendScheduledMsg() {
        String msg = "幸运大抽奖号:[" + UUID.randomUUID().toString().substring(0, 32) + "]";
        jmsMessagingTemplate.convertAndSend(topic, msg);
        logger.info("发送" + msg + "成功！！");
    }
}