package com.manerfan.waka.waki.auth.support.dao.role.repository;

import com.manerfan.waka.waki.auth.support.dao.role.model.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRoleRepository
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}
