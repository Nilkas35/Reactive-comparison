package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

interface CartRepository extends JpaRepository<Cart, Long> {

}
