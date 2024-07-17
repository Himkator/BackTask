package com.example.cloud.eureka.backtask.services;

import com.example.cloud.eureka.backtask.configs.JWTAuthFilter;
import com.example.cloud.eureka.backtask.dto.ProductDTO;
import com.example.cloud.eureka.backtask.entities.Product;
import com.example.cloud.eureka.backtask.entities.User;
import com.example.cloud.eureka.backtask.factories.ProductDTOFactory;
import com.example.cloud.eureka.backtask.repositories.ProductRepository;
import com.example.cloud.eureka.backtask.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final JWTUtils jwtUtils;
    private final ProductDTOFactory productDTOFactory;
    private final UserRepository userRepository;

    private ProductDTO errorDTO(String text){
        ProductDTO productDTO=new ProductDTO();
        productDTO.setStatusCode(500);
        productDTO.setMessage(text);
        return productDTO;
    }

    public List<ProductDTO> getAllProducts(){
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        return user.getProducts().stream()
                .map(productDTOFactory::makeProductDTO).toList();
    }

    public ProductDTO getProductById(Long id){
        ProductDTO productDTO;
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        Product product=productRepository.findById(id).orElse(null);
        if(product==null)return errorDTO("Product doesnt exist");
        if(!user.getProducts().contains(product) && !user.getRole().equals("Admin"))return errorDTO("User hasnt this product");
        return productDTOFactory.makeProductDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO){
        if((productDTO.getCost()+"").isBlank() ||
                (productDTO.getCount()+"").isBlank() ||
                (productDTO.getName()+"").isBlank()) return errorDTO("Product isn't complete");
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        List<String> products=user.getProducts().stream()
                .map(Product::getName).toList();
        if(products.contains(productDTO.getName())){
            Product product=productRepository.findByUserAndName(user, productDTO.getName());
            if(product.getCost()==productDTO.getCost()){
                return errorDTO("Product is exist, you cant create it");
            }
        }
        Product product=productDTOFactory.makeProductFromDTO(productDTO);
        product=productRepository.saveAndFlush(product);
        user.getProducts().add(product);
        productDTO=productDTOFactory.makeProductDTO(product);
        productDTO.setStatusCode(200);
        productDTO.setMessage("Product was created successfully");
        return productDTO;
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO){
        if((productDTO.getCost()+"").isBlank() ||
                (productDTO.getCount()+"").isBlank() ||
                (productDTO.getName()+"").isBlank()) return errorDTO("Product isn't complete");
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        Product product=productRepository.findById(id).orElse(null);
        if(product==null)return errorDTO("Product doesnt exist");
        if(!user.getProducts().contains(product) && !user.getRole().equals("Admin"))return errorDTO("User hasnt this product");
        product.setName(productDTO.getName());
        product.setCount(productDTO.getCount());
        product.setCost(productDTO.getCost());
        product.setUpdatedAt(Instant.now());
        product=productRepository.saveAndFlush(product);
        user.getProducts().add(product);

        productDTO=productDTOFactory.makeProductDTO(product);
        productDTO.setStatusCode(200);
        productDTO.setMessage("Product was successfully updated");

        return productDTO;
    }

    public ProductDTO deleteProduct(Long id){
        User user=userRepository.findByEmail(jwtUtils.extractUsername(JWTAuthFilter.jwt));
        Product product=productRepository.findById(id).orElse(null);
        if(product==null)return errorDTO("Product doesnt exist");
        if(!user.getProducts().contains(product) && !user.getRole().equals("Admin"))return errorDTO("User hasnt this product");

        productRepository.delete(product);
        user.getProducts().remove(product);
        ProductDTO productDTO=new ProductDTO();
        productDTO.setStatusCode(200);
        productDTO.setMessage("Product was successfully deleted");
        return productDTO;
    }
}
