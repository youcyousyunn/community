package com.ycs.community.sysbo.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class QueueConsumer {
    Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

    @JmsListener(destination = "${queue.queue-name}")
    public void receiveMsg(TextMessage message) throws JMSException {
        logger.info("快递部收到" + message.getText());
    }
}