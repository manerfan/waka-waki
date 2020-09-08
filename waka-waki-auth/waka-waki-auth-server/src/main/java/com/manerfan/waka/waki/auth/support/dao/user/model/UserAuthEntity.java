package com.manerfan.waka.waki.auth.support.dao.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.manerfan.waka.waki.auth.support.dao.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserAuth
 *
 * 用户认证信息
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_auth")
public class UserAuthEntity extends BaseEntity {
    /**
     * 唯一标识
     */
    @Column(nullable = false)
    private String uid;

    /**
     * 认证类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type", nullable = false)
    private UserIdentityType identityType;

    /**
     * 认证标识
     *
     * @see #identityType
     */
    @Column(nullable = false)
    private String identifier;

    /**
     * 认证 密码 token 等
     */
    @Column
    private String credential;
}
