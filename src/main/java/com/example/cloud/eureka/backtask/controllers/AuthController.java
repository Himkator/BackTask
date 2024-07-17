package com.example.cloud.eureka.backtask.controllers;


import com.example.cloud.eureka.backtask.dto.ReqRes;
import com.example.cloud.eureka.backtask.services.AuthService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUp){
        return ResponseEntity.ok(authService.signUp(signUp));
    }

    @PostMapping("/login")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signIn){
        return ResponseEntity.ok(authService.signIn(signIn));
    }
}
