package com.ycs.community.sysbo.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;

@Component
public class QueueProducer {
    private Logger logger = LoggerFactory.getLogger(QueueProducer.class);
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    /**
     * 发送消息
     * @param msg
     */
    public void sendMsg(String msg) {
        jmsMessagingTemplate.convertAndSend(queue, msg);
    }

    /**
     * 间隔时间发送消息
     */
//    @Scheduled(fixedDelay = 3000)
    public void sendScheduledMsg() {
        String msg = "订单号:[" + UUID.randomUUID().toString().substring(0, 32) + "]";
        jmsMessagingTemplate.convertAndSend(queue, msg);
        logger.info("发送" + msg + "成功！！");
    }
}