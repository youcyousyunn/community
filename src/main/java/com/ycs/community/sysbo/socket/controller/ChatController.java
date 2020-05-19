package com.ycs.community.sysbo.socket.controller;

import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.sysbo.socket.WebSocketServer;
import com.ycs.community.sysbo.socket.domain.po.ChatMessagePo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    /**
     * 发送消息给指定客户
     * @param chatMessagePo
     */
    @AnonymousAccess
    @PostMapping("/chat/client")
    public void sendMsgToUser(@RequestBody ChatMessagePo chatMessagePo) {
        WebSocketServer.sendMsgToClient(chatMessagePo);
    }

    /**
     * 发送消息给客服
     * @param chatMessagePo
     */
    @AnonymousAccess
    @PostMapping("/chat/service")
    public void sendMsgToService(@RequestBody ChatMessagePo chatMessagePo) {
        WebSocketServer.sendMsgToService(chatMessagePo);
    }

    /**
     * 发送消息给所有用户
     * @param chatMessagePo
     */
    @AnonymousAccess
    @PostMapping("/chat/users")
    public void sendMsgToUsers(@RequestBody ChatMessagePo chatMessagePo) {
        WebSocketServer.sendMsgToUsers(chatMessagePo);
    }
}
