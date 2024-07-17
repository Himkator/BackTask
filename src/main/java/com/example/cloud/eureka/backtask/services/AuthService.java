package com.example.cloud.eureka.backtask.services;

import com.example.cloud.eureka.backtask.dto.ReqRes;
import com.example.cloud.eureka.backtask.entities.User;
import com.example.cloud.eureka.backtask.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes registerResponse=new ReqRes();
        try{
            if(userRepository.findByEmail(registrationRequest.getEmail())!=null){
                registerResponse.setStatusCode(500);
                registerResponse.setError("User is exist");
                return registerResponse;
            }
            User user=new User();
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(registrationRequest.getRole());
            User user1=userRepository.saveAndFlush(user);
            if(user1!=null && user1.getId()>0){
                registerResponse.setStatusCode(200);
                registerResponse.setMessage("User saved successfully");
                registerResponse.setUser(user1);
            }
        }catch (Exception e){
            registerResponse.setStatusCode(500);
            registerResponse.setError(e.getMessage());
        }
        return registerResponse;
    }

    public ReqRes signIn(ReqRes loginRequest){
        ReqRes loginResponse=new ReqRes();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail());
            System.out.println("USER IS: "+user);
            var jwt=jwtUtils.generateToken(user);
            var refreshToken=jwtUtils.generateRefreshToken(new HashMap<>(), user);
            loginResponse.setStatusCode(200);
            loginResponse.setToken(jwt);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setExpirationTime("24Hr");
            loginResponse.setMessage("Successfully signed in");
        }catch (Exception e){
            loginResponse.setStatusCode(500);
            loginResponse.setError(e.getMessage());
        }
        return loginResponse;
    }

    public ReqRes refreshToken(ReqRes refreshRequest){
        ReqRes refresh=new ReqRes();
        User user=userRepository.findByEmail(jwtUtils.extractUsername(refreshRequest.getToken()));
        if(jwtUtils.isTokenValid(refreshRequest.getToken(), user)){
            var jwt=jwtUtils.generateToken(user);
            refresh.setStatusCode(200);
            refresh.setToken(jwt);
            refresh.setRefreshToken(refreshRequest.getToken());
            refresh.setExpirationTime("24Hr");
            refresh.setMessage("Successfully Refreshed Token");
            return refresh;
        }
        refresh.setStatusCode(500);
        refresh.setMessage("Fail");
        return refresh;
    }
}
