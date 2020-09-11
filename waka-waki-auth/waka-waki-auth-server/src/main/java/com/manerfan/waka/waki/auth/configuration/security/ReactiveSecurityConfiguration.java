package com.manerfan.waka.waki.auth.configuration.security;

import com.manerfan.waka.waki.auth.support.component.user.UserReadComponent;
import com.manerfan.waka.waki.auth.support.dao.user.model.UserIdentityType;
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
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver.DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME;

/**
 * SecurityConfig
 *
 * https://docs.github.com/en/developers/apps/building-oauth-apps
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@EnableWebFluxSecurity
public class ReactiveSecurityConfiguration {
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

    /**
     * 角色继承
     *
     * @return {@link RoleHierarchy}
     */
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
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
        ReactiveClientRegistrationRepository clientRegistrationRepository) {
        pathPermit(http);
        formLogin(http);
        oauth2(http, clientRegistrationRepository);
        logout(http);
        csrf(http);
        exception(http);
        return http.build();
    }

    private ServerHttpSecurity pathPermit(ServerHttpSecurity http) {
        return http.authorizeExchange()
            .pathMatchers("/login/**").permitAll()
            .pathMatchers("/logout").permitAll()
            .anyExchange().authenticated()
            .and();
    }

    private ServerHttpSecurity formLogin(ServerHttpSecurity http) {
        return http.formLogin()
            //.requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/signin"))
            // 登录成功
            .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                val principal = authentication.getPrincipal();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap("DONE".getBytes())));
            })
            // 登录失败
            .authenticationFailureHandler((webFilterExchange, exception) -> {
                val message = exception.getMessage();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
            })
            .and();
    }

    private ServerHttpSecurity oauth2(ServerHttpSecurity http,
        ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http.oauth2Login()
            .authorizationRequestResolver(
                new DefaultServerOAuth2AuthorizationRequestResolver(
                    clientRegistrationRepository,
                    new PathPatternParserServerWebExchangeMatcher(
                        "/login/oauth2/authorization/{" + DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME + "}")
                )
            ).and();
    }

    private ServerHttpSecurity logout(ServerHttpSecurity http) {
        return http.logout()
            //.requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/signout"))
            .logoutSuccessHandler((webFilterExchange, authentication) -> {
                val principal = authentication.getPrincipal();
                val response = webFilterExchange.getExchange().getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap("DONE".getBytes())));
            })
            .and();
    }

    private ServerHttpSecurity csrf(ServerHttpSecurity http) {
        return http.csrf().disable();
    }

    private ServerHttpSecurity exception(ServerHttpSecurity http) {
        return http.exceptionHandling()
            // 未登录
            .authenticationEntryPoint((serverWebExchange, exception) -> {
                val message = exception.getMessage();
                val response = serverWebExchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
            })
            .and();
    }
}
