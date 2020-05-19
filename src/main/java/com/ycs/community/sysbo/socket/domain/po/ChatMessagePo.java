package com.ycs.community.sysbo.socket.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import com.ycs.community.sysbo.socket.enums.SocketStatus;
import lombok.Data;

@Data
public class ChatMessagePo extends BasePo {
    private Long serviceId;
    private Long clientId;
    private String serviceName;
    private String clientName;
    private String msg;
    private String role;
    private SocketStatus status;
}
