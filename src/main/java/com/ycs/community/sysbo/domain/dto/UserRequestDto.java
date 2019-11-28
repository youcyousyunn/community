package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class UserRequestDto extends BaseRequestDto {
    private Long id;
    private String code;
    private String uuid;
    private Long accountId;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String token;
    private String avatarUrl;
    private boolean enabled;
    private Long lastPasswordResetTime;
    private List<RolePo> roles;
    private DeptPo dept;
    private JobPo job;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (null == name || StringUtils.isEmpty(name)) {
            return false;
        }
        if (null == password || StringUtils.isEmpty(password)) {
            return false;
        }
        if (null == code || StringUtils.isEmpty(code)) {
            return false;
        }
        if (null == uuid || StringUtils.isEmpty(uuid)) {
            return false;
        }
        return true;
    }

    /**
     * 新增、修改用户接口请求报文检查
     * @return
     */
    public boolean checkRequest2Dto() {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        if (StringUtils.isEmpty(enabled)) {
            return false;
        }
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        if (StringUtils.isEmpty(dept)) {
            return false;
        }
        if (StringUtils.isEmpty(job)) {
            return false;
        }
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        return true;
    }
}
