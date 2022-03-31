package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class CartRouter {
    private final CartRepository cartRepository;

    CartRouter(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> getAllCartsRoute() {
        return route(GET("/cart"),
                req ->
                    ok().body(
                            cartRepository.findAll(), Cart.class)
        );
    }
    @Bean
    RouterFunction<ServerResponse> updateCartRoute() {
        return route(POST("/cart"),
                req -> req.bodyToMono(Cart.class)
                        .flatMap(cart -> Mono.just(cartRepository.save(cart)))
                        .flatMap(cart -> ServerResponse.status(HttpStatus.CREATED)
                                .body(cart, Cart.class)));
    }
}

