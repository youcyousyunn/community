package com.ycs.community.spring.security.domain.po;

import com.ycs.community.sysbo.domain.po.RolePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserPo implements Serializable {
    private static final long serialVersionUID = 3065891170336873304L;
    private String key;
    private Long accountId;
    private String name;
    private String nickname;
    private int sex;
    private String phone;
    private String email;
    private String job;
    private String avatar;
    private List<RolePo> roles;
    private String requestIp;
    private String address;
    private String browser;
    private Date loginTm;
}