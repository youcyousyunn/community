package com.ycs.community.sysbo.socket;

import com.ycs.community.spring.config.SpringSocketConfigurator;
import com.ycs.community.sysbo.socket.domain.po.ChatMessagePo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/socket/{accountId}", configurator = SpringSocketConfigurator.class)
@Component
@Slf4j
@Data
public class WebSocketServer {
    // 静态变量, 记录当前在线连接数
    private static volatile AtomicInteger onlineCount = new AtomicInteger(0);
    // 存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet();
    // 与某个客户端的连接会话, 通过它给客户端发送数据
    private Session session;
    // 接收连接的用户ID
    private Long accountId = 0l;

    /**
     * 建立连接
     * @param session
     * @param accountId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("accountId") Long accountId) {
        WebSocketServer socketServer = new WebSocketServer();
        socketServer.setSession(session);
        socketServer.setAccountId(accountId);
        // 添加连接
        webSocketSet.add(socketServer);
        // 在线数加1
        addOnlineCount();
        log.info("新的用户上线了:" + accountId + ",当前在线人数为" + getOnlineCount());
        this.session = session;
        sendConnectMessage("连接成功");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param msg
     * @param session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        log.info("收到来自用户" + accountId + "的信息:" + msg);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            sendMessage(item.getSession(), msg);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生错误");
        throwable.printStackTrace();
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
     * 建立连接后服务器推动消息
     * @param msg
     */
    public void sendConnectMessage(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("websocket io异常");
        }
    }

    /**
     * 服务器推送消息
     * @param session
     * @param msg
     */
    public void sendMessage(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("websocket io异常");
        }
    }

    /**
     * 发送消息给指定用户
     * @param chatMessagePo
     */
    public static void sendMsgToUser(ChatMessagePo chatMessagePo) {
        webSocketSet.forEach(webSocketServer -> {
            if (chatMessagePo.getTo().equals(webSocketServer.accountId)) {
                webSocketServer.sendMessage(webSocketServer.getSession(), chatMessagePo.getContent());
            }
        });
    }

    /**
     * 发送消息给所有人
     * @param chatMessagePo
     */
    public static void sendMsgToUsers(ChatMessagePo chatMessagePo) {
        webSocketSet.forEach(webSocketServer -> {
            webSocketServer.sendMessage(webSocketServer.getSession(), chatMessagePo.getContent());
        });
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
