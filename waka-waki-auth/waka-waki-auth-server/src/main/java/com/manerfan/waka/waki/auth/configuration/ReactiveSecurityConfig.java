package com.manerfan.waka.waki.auth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manerfan.waka.waki.auth.support.component.user.UserReadComponent;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserIdentityType;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * SecurityConfig
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@EnableWebFluxSecurity
public class ReactiveSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetailsService
     */
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserReadComponent userReadComponent) {
        return username -> userReadComponent.findUserDetailsBy(UserIdentityType.LOGIN_ID, username);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    /**
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
     * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ObjectMapper mapper) {
        return http.authorizeExchange()
            .pathMatchers("/logout").permitAll()
            .anyExchange().authenticated()
            .and().formLogin()
            // 登录成功
            .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                val principal = authentication.getPrincipal();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                    Try.of(() -> mapper.writeValueAsBytes(principal)).getOrElse("NONE".getBytes())
                )));
            })
            // 登录失败
            .authenticationFailureHandler((webFilterExchange, exception) -> {
                val message = exception.getMessage();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
            })
            // 登出
            .and().logout().logoutSuccessHandler((webFilterExchange, authentication) -> {
                val principal = authentication.getPrincipal();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                    Try.of(() -> mapper.writeValueAsBytes(principal)).getOrElse("NONE".getBytes())
                )));
            })
            .and().csrf().disable()
            .exceptionHandling()
            // 未登录
            .authenticationEntryPoint((serverWebExchange, exception) -> {
                val message = exception.getMessage();
                val response = serverWebExchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
            })
            .and().build();
    }
}
