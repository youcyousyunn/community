package com.ycs.community.sysbo.socket.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class ChatUserPo extends BasePo {
    private Long accountId;
    private String nickname;
    private int sex;
    private String phone;
    private String email;
    private String avatar;
    private boolean isService;
    private boolean enabled;
}
