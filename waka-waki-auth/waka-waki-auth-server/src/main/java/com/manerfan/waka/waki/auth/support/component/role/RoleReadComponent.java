package com.manerfan.waka.waki.auth.support.component.role;

import java.util.List;

import com.manerfan.waka.waki.auth.support.dao.role.repository.UserRoleMapperRepository;
import com.manerfan.waka.waki.auth.support.dao.role.repository.UserRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * RoleReadComponent
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Component
public class RoleReadComponent {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapperRepository userRoleMapperRepository;

    public RoleReadComponent(UserRoleRepository userRoleRepository, UserRoleMapperRepository userRoleMapperRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapperRepository = userRoleMapperRepository;
    }

    /**
     * 根据用户唯一标识查询所有角色codes
     *
     * @param userUid 用户唯一标识
     * @return 角色codes
     */
    public Mono<List<String>> findAllRoleCodeByUserUid(String userUid) {
        if (StringUtils.isBlank(userUid)) {
            return Mono.empty();
        }

        return Mono.just(userRoleMapperRepository.findAllRoleCodesByUserUid(userUid));
    }
}
