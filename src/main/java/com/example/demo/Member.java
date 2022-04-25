package com.example.demo;

import lombok.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("members")
@Cacheable
public class Member {
    @Id
    private Long id;
    private String name;
}
