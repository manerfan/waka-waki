package com.manerfan.waka.waki.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * WakaWakiAuthApplication
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WakaWakiAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(WakaWakiAuthApplication.class, args);
    }
}
