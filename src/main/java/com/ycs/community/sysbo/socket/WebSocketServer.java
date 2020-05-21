package com.ycs.community.sysbo.socket;

import cn.hutool.json.JSONUtil;
import com.ycs.community.spring.config.SpringSocketConfigurator;
import com.ycs.community.sysbo.socket.domain.po.ChatMessagePo;
import com.ycs.community.sysbo.socket.enums.SocketRoleEnum;
import com.ycs.community.sysbo.socket.enums.SocketStatusEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/socket/{accountId}", configurator = SpringSocketConfigurator.class)
@Component
@Slf4j
@Data
public class WebSocketServer {
    // 静态变量, 记录当前在线连接数
    private static volatile AtomicInteger onlineCount = new AtomicInteger(0);
    // 存放每个客户端对应的WebSocket对象
    private static Map<Long, WebSocketServer> concurrentHashMap = new ConcurrentHashMap();
    // 与某个客户端的连接会话, 通过它给客户端发送数据
    private Session session;
    // 接收连接的用户ID
    private Long accountId = 0l;
    private SocketRoleEnum role = SocketRoleEnum.CLIENT;

    /**
     * 建立连接
     * @param session
     * @param accountId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("accountId") Long accountId) {
        WebSocketServer socket = new WebSocketServer();
        socket.setSession(session);
        socket.setAccountId(accountId);
        if(accountId == 9527l) {
            socket.setRole(SocketRoleEnum.SERVER);
        }
        // 添加连接
        if(concurrentHashMap.containsKey(accountId)) {
            concurrentHashMap.remove(accountId);
            concurrentHashMap.put(accountId, socket);
        } else {
            concurrentHashMap.put(accountId, socket);
        }
        // 在线数加1
        addOnlineCount();
        log.info("新的用户上线了:" + accountId + ",当前在线人数为" + getOnlineCount());
//        this.session = session;
        ChatMessagePo message = new ChatMessagePo();
        message.setServiceId(9527l);
        message.setServiceName("太阳晒屁股了");
        if(accountId != 9527l) {
            message.setClientId(accountId);
            message.setClientName("唐伯虎");
            message.setContent("客户上线啦");
            message.setRole(SocketRoleEnum.CLIENT);
            message.setStatus(SocketStatusEnum.OPEN);
            notifyUsers(message);
        } else {
            message.setContent("客服上线啦");
            message.setRole(SocketRoleEnum.SERVER);
            message.setStatus(SocketStatusEnum.OPEN);
            notifyUser(message);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param msg
     * @param session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        ///todo
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        for(WebSocketServer socket : concurrentHashMap.values()) {
            if(session.equals(socket.getSession())) {
                concurrentHashMap.remove(socket.getAccountId());
                log.error("连接发生错误");
            }
        }
        throwable.printStackTrace();
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        // 在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 通知服务端
     * @param message
     */
    public synchronized void notifyUser(ChatMessagePo message) {
        concurrentHashMap.values().forEach(socket -> {
            if(socket.getRole().equals(SocketRoleEnum.SERVER)) {
                try {
                    socket.getSession().getBasicRemote().sendText(JSONUtil.toJsonStr(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 同时通知服务端&客户端
     * @param message
     */
    public synchronized void notifyUsers(ChatMessagePo message) {
        concurrentHashMap.values().forEach(socket -> {
            if(socket.getAccountId().equals(message.getClientId()) || socket.getRole().equals(SocketRoleEnum.SERVER)) {
                try {
                    socket.getSession().getBasicRemote().sendText(JSONUtil.toJsonStr(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
     * 发送消息给指定客户
     * @param message
     */
    public static void sendMsgToClient(ChatMessagePo message) {
        concurrentHashMap.values().forEach(socket -> {
            if (message.getClientId().equals(socket.getAccountId())) {
                socket.sendMessage(socket.getSession(), JSONUtil.toJsonStr(message));
            }
        });
    }

    /**
     * 发送消息给客服
     * @param message
     */
    public static void sendMsgToService(ChatMessagePo message) {
        concurrentHashMap.values().forEach(socket -> {
            if (message.getServiceId().equals(socket.accountId)) {
                socket.sendMessage(socket.getSession(), JSONUtil.toJsonStr(message));
            }
        });
    }

    /**
     * 发送消息给所有人
     * @param message
     */
    public static void sendMsgToUsers(ChatMessagePo message) {
        concurrentHashMap.values().forEach(socket -> {
            socket.sendMessage(socket.getSession(), JSONUtil.toJsonStr(message));
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
