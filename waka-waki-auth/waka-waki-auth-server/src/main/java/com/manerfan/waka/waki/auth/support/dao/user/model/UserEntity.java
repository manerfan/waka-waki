package com.manerfan.waka.waki.auth.support.dao.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.manerfan.waka.waki.auth.support.dao.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserEntity
 *
 * 用户信息
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    /**
     * 唯一标识
     */
    @Column(nullable = false)
    private String uid;

    /**
     * 昵称
     */
    @Column
    private String nickname;

    /**
     * 头像
     */
    @Column
    private String avatar;
}
