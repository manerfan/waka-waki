package com.manerfan.waka.waki.auth.configuration.oauth;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * OauthClientConfiguration
 *
 * <pre>
 * 如果需要SSO (Oauth Support)，可以参考
 * <a href="https://www.keycloak.org/">keycloak</a>
 * <a href="https://spring.io/blog/2020/04/15/announcing-the-spring-authorization-server">announcing-the-spring-authorization-server</a>
 * 等方案建立Oauth Server
 *
 * Spring Security Oauth 不再计划支持 Auth Server
 * <a href="https://spring.io/blog/2019/11/14/spring-security-oauth-2-0-roadmap-update">Spring Security Oauth 2.0 Roadmap</a>
 *
 * @author Maner.Fan
 * @date 2020/9/10
 */
@Configuration
public class OauthClientConfiguration {
    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepo(
        @Value("${security.oauth2.client.registration.github.client-id:YourGitHubClientId}") String clientId,
        @Value("${security.oauth2.client.registration.github.client-secret:YourGitHubSecretId}") String clientSecret
    ) {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
            .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope("read:user")
            .authorizationUri("https://github.com/login/oauth/authorize")
            .tokenUri("https://github.com/login/oauth/access_token")
            .userInfoUri("https://api.github.com/user")
            .userNameAttributeName("id")
            .clientName("github")
            // default {baseUrl}/login/oauth2/code/{registrationId}
            .redirectUriTemplate("{baseUrl}/api/auth/login/oauth2/code/{registrationId}")
            .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public WebClient oauthWebClient(
        ReactiveClientRegistrationRepository clientRegistrationRepo,
        ServerOAuth2AuthorizedClientRepository authorizedClientRepo) {
        val filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
            clientRegistrationRepo, authorizedClientRepo);

        return WebClient.builder().filter(filter).build();
    }
}
