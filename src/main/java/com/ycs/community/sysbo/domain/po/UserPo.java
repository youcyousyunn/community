package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserPo extends BasePo implements UserDetails {
    private Long id;
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
    private Long deptId;
    private Long jobId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
