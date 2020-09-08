package com.manerfan.waka.waki.auth.support.dao.role.repository;

import java.util.List;

import com.manerfan.waka.waki.auth.support.dao.role.model.UserRoleMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * UserRoleMapperRepository
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Repository
public interface UserRoleMapperRepository extends JpaRepository<UserRoleMapper, Long> {
    /**
     * 查找用户的所有角色
     *
     * @param userUid 用户唯一标识
     * @return role codes
     */
    @Query("SELECT urm.roleCode FROM UserRoleMapper AS urm WHERE urm.userUid = ?1")
    List<String> findAllRoleCodesByUserUid(String userUid);
}
