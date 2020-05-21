package com.ycs.community.sysbo.socket.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import com.ycs.community.sysbo.socket.enums.SocketRoleEnum;
import com.ycs.community.sysbo.socket.enums.SocketStatusEnum;
import lombok.Data;

@Data
public class ChatMessagePo extends BasePo {
    private Long serviceId;
    private String serviceName;
    private String serviceAvatarUrl;
    private Long clientId;
    private String clientName;
    private String clientAvatarUrl;
    private String content;
    private String contentType;
    private SocketRoleEnum role;
    private SocketStatusEnum status;
}
