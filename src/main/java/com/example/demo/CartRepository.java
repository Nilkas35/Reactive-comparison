package com.example.demo;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveSortingRepository<Cart, Long> {
}
