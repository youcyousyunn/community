package com.ycs.community.sysbo.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class TopicConsumer {
    Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    @JmsListener(destination = "${topic.topic-name}")
    public void receiveMsg(TextMessage message) throws JMSException {
        logger.info("订阅号收到" + message.getText());
    }
}