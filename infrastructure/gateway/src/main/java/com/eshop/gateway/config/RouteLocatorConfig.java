package com.eshop.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RouteLocatorConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("catalog", r -> r
            .path("/api/v1/catalog/*")
            .filters(f -> f.removeRequestHeader("Cookie") // Prevents cookie being sent downstream
//                .circuitBreaker(config -> config.setName("normalflux"))
                .rewritePath("api/v1", ""))
            .uri("lb://catalog") // Take advantage of docker naming
        )
        .route("basket", r -> r
            .order(0)
            .path("/api/v1/basket/*")
            .filters(f -> f.removeRequestHeader("Cookie")
                .rewritePath("api/v1", "")
            )
            .uri("lb://basket")
        )
        .route("orders", r -> r
            .path("/api/v1/orders/*")
            .filters(f -> f.removeRequestHeader("Cookie")
                .rewritePath("api/v1", "")
            )
            .uri("lb://order-processing")
        )
        .build();
  }

}
