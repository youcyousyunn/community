package com.ycs.community.spring.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Component
public class IWebSocketHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(IWebSocketHandler.class);
    private static final List<WebSocketSession> webSocketSessionList = new LinkedList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        webSocketSessionList.add(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        logger.info("WebSocket handle message{}", webSocketMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        webSocketSessionList.remove(webSocketSession);
        logger.error("handle transport error{}", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        webSocketSessionList.remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送消息给指定用户
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        Iterator<WebSocketSession> iterator = webSocketSessionList.iterator();
        while (iterator.hasNext()) {
            WebSocketSession webSocketSession =  iterator.next();
            if (userId.equals(webSocketSession.getAttributes().get("userId"))) {
                try {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 发送消息给所有用户
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        Iterator<WebSocketSession> iterator = webSocketSessionList.iterator();
        while (iterator.hasNext()) {
            WebSocketSession webSocketSession = iterator.next();
            try {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}