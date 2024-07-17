package com.example.cloud.eureka.backtask.factories;

import com.example.cloud.eureka.backtask.configs.JWTAuthFilter;
import com.example.cloud.eureka.backtask.dto.ProductDTO;
import com.example.cloud.eureka.backtask.entities.Product;
import com.example.cloud.eureka.backtask.entities.User;
import com.example.cloud.eureka.backtask.repositories.ProductRepository;
import com.example.cloud.eureka.backtask.repositories.UserRepository;
import com.example.cloud.eureka.backtask.services.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDTOFactory {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductDTO makeProductDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .cost(product.getCost())
                .count(product.getCount())
                .build();
    }

    public Product makeProductFromDTO(ProductDTO productDTO){
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        return Product.builder()
                .name(productDTO.getName())
                .cost(productDTO.getCost())
                .count(productDTO.getCount())
                .user(user)
                .build();
    }
}
