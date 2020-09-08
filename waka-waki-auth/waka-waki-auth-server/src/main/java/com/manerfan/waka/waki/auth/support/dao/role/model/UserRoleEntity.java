package com.manerfan.waka.waki.auth.support.dao.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.manerfan.waka.waki.auth.support.dao.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserRoleEntity
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_role")
public class UserRoleEntity extends BaseEntity {
    /**
     * 角色码
     */
    @Column(nullable = false)
    private String code;

    /**
     * 角色名
     */
    @Column(nullable = false)
    private String name;
}
