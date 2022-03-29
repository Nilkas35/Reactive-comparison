package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@Cacheable
public class Product extends PanacheEntity {
    @Column(length = 250, unique = true)
    public String name;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }
}
