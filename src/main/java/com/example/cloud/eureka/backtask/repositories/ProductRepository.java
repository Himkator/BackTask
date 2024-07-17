package com.example.cloud.eureka.backtask.repositories;

import com.example.cloud.eureka.backtask.entities.Product;
import com.example.cloud.eureka.backtask.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByUserAndName(User user, String name);
}
