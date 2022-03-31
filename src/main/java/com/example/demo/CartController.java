package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class CartController {

    private final CartRepository repository;

    CartController(CartRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cart")
    List<Cart> all() {
        return repository.findAll();
    }

    @PostMapping("/cart")
    Cart newCart(@RequestBody Cart newCart) {
        return repository.save(newCart);
    }

    @GetMapping("/cart/{id}")
    Cart one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PutMapping("/cart/{id}")
    Cart replaceCart(@RequestBody Cart newCart, @PathVariable Long id) {

        return repository.findById(id)
                .map(cart -> {
                    cart.setName(newCart.getName());
                    return repository.save(cart);
                })
                .orElseGet(() -> {
                    newCart.setId(id);
                    return repository.save(newCart);
                });
    }

    @DeleteMapping("/cart/{id}")
    void deleteCart(@PathVariable Long id) {
        repository.deleteById(id);
    }
}