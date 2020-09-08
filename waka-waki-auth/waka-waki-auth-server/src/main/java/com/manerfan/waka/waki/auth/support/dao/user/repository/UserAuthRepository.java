package com.manerfan.waka.waki.auth.support.dao.user.repository;

import com.manerfan.waka.waki.auth.support.dao.user.model.UserAuthEntity;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserIdentityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserAuthRepository
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {

    /**
     * 查询用户认证信息
     *
     * @param identityType 认证类型
     * @param identifier   认证标识
     * @return {@link UserAuthEntity}
     */
    UserAuthEntity findOneByIdentityTypeAndIdentifier(UserIdentityType identityType, String identifier);
}
