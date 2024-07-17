package com.example.cloud.eureka.backtask.factories;

import com.example.cloud.eureka.backtask.dto.UserDTO;
import com.example.cloud.eureka.backtask.entities.User;
import com.example.cloud.eureka.backtask.repositories.ProductRepository;
import com.example.cloud.eureka.backtask.services.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDTOFactory {
    private final JWTUtils jwtUtils;
    private final ProductDTOFactory productDTOFactory;

    public UserDTO makeUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .productDTOS(user.getProducts()
                        .stream()
                        .map(productDTOFactory::makeProductDTO)
                        .toList())
                .build();
    }
}
