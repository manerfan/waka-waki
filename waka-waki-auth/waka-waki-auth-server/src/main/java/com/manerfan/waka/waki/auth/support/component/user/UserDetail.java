package com.manerfan.waka.waki.auth.support.component.user;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserAuthEntity;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserEntity;
import lombok.Builder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetail for security
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
public class UserDetail implements UserDetails {
    /**
     * 用户信息
     */
    private UserEntity userEntity;

    /**
     * 用户认证信息
     */
    private UserAuthEntity userAuthEntity;

    /**
     * 用户角色
     */
    private List<GrantedAuthority> authorities;

    @Builder
    public UserDetail(UserEntity userEntity,
        UserAuthEntity userAuthEntity,
        List<String> roles) {
        this.userEntity = userEntity;
        this.userAuthEntity = userAuthEntity;
        this.authorities = CollectionUtils.emptyIfNull(roles).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return Objects.nonNull(userAuthEntity) ? userAuthEntity.getCredential() : null;
    }

    @Override
    public String getUsername() {
        return Objects.nonNull(userAuthEntity) ? userAuthEntity.getIdentifier() : null;
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
        return true;
    }
}
