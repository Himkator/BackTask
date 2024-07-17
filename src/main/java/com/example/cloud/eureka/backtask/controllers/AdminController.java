package com.example.cloud.eureka.backtask.controllers;

import com.example.cloud.eureka.backtask.dto.UserDTO;
import com.example.cloud.eureka.backtask.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, UserDTO userDTO){
        return ResponseEntity.ok(adminService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(adminService.deleteUser(id));
    }
}
