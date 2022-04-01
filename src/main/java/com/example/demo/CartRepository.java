package com.example.demo;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository extends ReactiveSortingRepository<Cart, Long> {
    Mono<Cart> findCartById(String id);
}
