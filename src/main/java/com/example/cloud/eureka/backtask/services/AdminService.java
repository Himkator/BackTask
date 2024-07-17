package com.example.cloud.eureka.backtask.services;

import com.example.cloud.eureka.backtask.dto.UserDTO;
import com.example.cloud.eureka.backtask.entities.User;
import com.example.cloud.eureka.backtask.factories.UserDTOFactory;
import com.example.cloud.eureka.backtask.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final UserDTOFactory userDTOFactory;

    private UserDTO errorUser(String text){
        UserDTO userDTO=new UserDTO();
        userDTO.setStatusCode(500);
        userDTO.setMessage(text);
        return userDTO;
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userDTOFactory::makeUserDTO)
                .toList();
    }

    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user=userRepository.findById(id).orElse(null);
        if(user==null) errorUser("User isnt exist");
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user=userRepository.saveAndFlush(user);
        userDTO=userDTOFactory.makeUserDTO(user);
        userDTO.setStatusCode(200);
        userDTO.setMessage("User was successfully updated");
        return userDTO;
    }

    public UserDTO deleteUser(Long id){
        User user=userRepository.findById(id).orElse(null);
        if(user==null) errorUser("User isnt exist");

        userRepository.delete(user);
        UserDTO userDTO=new UserDTO();
        userDTO.setStatusCode(200);
        userDTO.setMessage("User was successfully deleted");
        return userDTO;
    }
}
