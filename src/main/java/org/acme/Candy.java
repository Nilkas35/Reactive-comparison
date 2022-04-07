package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
//@Cacheable
public class Candy extends PanacheEntity {
    @Column(length = 40)
    public String name;

    public Candy() {
    }

    public Candy(String name) {
        this.name = name;
    }
}
