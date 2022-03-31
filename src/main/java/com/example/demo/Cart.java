package com.example.demo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="carts")
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart carts = (Cart) o;
        return id == carts.id && Objects.equals(name, carts.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
