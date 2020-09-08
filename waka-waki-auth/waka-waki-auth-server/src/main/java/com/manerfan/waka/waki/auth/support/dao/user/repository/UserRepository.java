package com.manerfan.waka.waki.auth.support.dao.user.repository;

import com.manerfan.waka.waki.auth.support.dao.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * 查询用户信息
     *
     * @param uid 用户唯一标识
     * @return {@link UserEntity}
     */
    UserEntity findOneByUid(String uid);
}
