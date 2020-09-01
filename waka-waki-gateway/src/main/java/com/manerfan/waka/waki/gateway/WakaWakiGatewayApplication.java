package com.manerfan.waka.waki.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Waka Waki Gateway
 *
 * @author manerfan
 * @date 2020-09-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WakaWakiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WakaWakiGatewayApplication.class, args);
    }
}
