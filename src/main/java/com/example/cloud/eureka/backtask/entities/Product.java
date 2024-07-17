package com.example.cloud.eureka.backtask.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name="products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double cost;
    @Column(nullable = false)
    private int count;
    @ManyToOne
    @JoinColumn
    private User user;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    Instant updatedAt = Instant.now();
}
