package com.awbd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Date;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventoryManagement_route", r -> r
                        .path("/awbd/inventoryManagement/**")
                        .filters(f -> f.rewritePath("/awbd/inventoryManagement/(?<segment>.*)", "/${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://INVENTORYMANAGEMENT"))
                .route("statistics_route", r -> r
                        .path("/awbd/statistics/**")
                        .filters(f -> f.rewritePath("/awbd/statistics/(?<segment>.*)", "/${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://STATISTICS"))

                .build();
    }


}
