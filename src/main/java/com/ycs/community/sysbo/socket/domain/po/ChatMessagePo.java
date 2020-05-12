package com.ycs.community.sysbo.socket.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class ChatMessagePo extends BasePo {
    private String title;
    private String content;
    private Long from;
    private Long to;
}
