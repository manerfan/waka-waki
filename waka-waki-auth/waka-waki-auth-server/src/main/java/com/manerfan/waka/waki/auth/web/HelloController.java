package com.manerfan.waka.waki.auth.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * HelloController
 *
 * @author Maner.Fan
 * @date 2020/9/9
 */
@RestController
public class HelloController {
    @GetMapping
    public Mono<String> hello() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .switchIfEmpty(Mono.just("NONE"));
    }
}
