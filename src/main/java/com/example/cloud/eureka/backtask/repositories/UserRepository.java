package com.example.cloud.eureka.backtask.repositories;

import com.example.cloud.eureka.backtask.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
