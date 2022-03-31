package com.example.demo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("carts")
public class Cart {
    @Id
    private Long id;
    private String name;
}
