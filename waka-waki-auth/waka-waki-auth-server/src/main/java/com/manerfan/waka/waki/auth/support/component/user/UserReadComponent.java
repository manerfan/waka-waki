package com.manerfan.waka.waki.auth.support.component.user;

import java.util.Objects;

import com.manerfan.waka.waki.auth.support.component.role.RoleReadComponent;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserIdentityType;
import com.manerfan.waka.waki.auth.support.dao.user.repository.UserAuthRepository;
import com.manerfan.waka.waki.auth.support.dao.user.repository.UserRepository;
import io.vavr.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UserReadComponent
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Component
public class UserReadComponent {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final RoleReadComponent roleReadComponent;

    public UserReadComponent(
        UserRepository userRepository,
        UserAuthRepository userAuthRepository,
        RoleReadComponent roleReadComponent) {
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
        this.roleReadComponent = roleReadComponent;
    }

    /**
     * 查询用于Security的用户信息
     *
     * @param identityType 认证类型
     * @param identifier   认证标识
     * @return {@link UserDetails}
     */
    public Mono<UserDetails> findUserDetailsBy(UserIdentityType identityType, String identifier) {
        if (Objects.isNull(identityType) || StringUtils.isBlank(identifier)) {
            return Mono.empty();
        }

        return Mono
            // 认证信息
            .fromCallable(() -> userAuthRepository.findOneByIdentityTypeAndIdentifier(identityType, identifier))
            .filter(Objects::nonNull)
            // 用户信息
            .map(userAuth -> Tuple.of(userRepository.findOneByUid(userAuth.getUid()), userAuth))
            .filter(userType -> Objects.nonNull(userType._1))
            // 角色 & 组装
            .flatMap(userTuple ->
                // 角色
                roleReadComponent.findAllRoleCodeByUserUid(userTuple._1().getUid())
                    // 组装
                    .map(roles -> UserDetail.builder()
                        .userEntity(userTuple._1)
                        .userAuthEntity(userTuple._2)
                        .roles(roles)
                        .build()
                    )
            );
    }
}
