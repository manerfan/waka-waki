package com.manerfan.waka.waki.auth.support.dao.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.manerfan.waka.waki.auth.support.dao.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserRoleMapper
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_role_mapper")
public class UserRoleMapper extends BaseEntity {
    /**
     * 用户唯一标识
     */
    @Column(name = "user_uid", nullable = false)
    private String userUid;

    /**
     * 角色码
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;
}
