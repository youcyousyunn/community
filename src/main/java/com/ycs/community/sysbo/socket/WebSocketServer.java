package com.ycs.community.sysbo.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/socket/userId")
@Component
@Slf4j
public class WebSocketServer {
    // 静态变量, 记录当前在线连接数
    private static volatile AtomicInteger onlineCount = new AtomicInteger(0);
    // 存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet();
    // 与某个客户端的连接会话, 通过它给客户端发送数据
    private Session session;
    // 接收连接的用户ID
    private String userId = "";


    /**
     * 建立连接
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        // 添加连接
        webSocketSet.add(this);
        // 在线数加1
        addOnlineCount();
        log.info("新的用户上线了:" + userId + ",当前在线人数为" + getOnlineCount());
        this.userId = userId;
        sendMessage("连接成功");
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        // 移除连接
        webSocketSet.remove(this);
        // 在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 服务器推动消息
     * @param msg
     */
    public void sendMessage(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("websocket io异常");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param msg
     * @param session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        log.info("收到来自用户" + userId + "的信息:" + msg);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage(msg);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生错误");
        throwable.printStackTrace();
    }

    public static synchronized void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.getAndDecrement();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }
}
