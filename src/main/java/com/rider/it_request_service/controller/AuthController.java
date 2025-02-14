package com.rider.it_request_service.controller;

import com.rider.it_request_service.dto.UserDTO;
import com.rider.it_request_service.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) {
        try {
            String token = authService.login(userDTO.username(), userDTO.password());
            return ResponseEntity.ok().body(Map.of("token", "Bearer " + token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // สำหรับ hash password เนื่องจากไม่ได้ทำ register
    @PostMapping("/hash-passwords")
    public ResponseEntity<?> hashPasswords() {
        authService.hashPasswords(); // เรียกใช้งาน hashPasswords
        return ResponseEntity.ok("Passwords hashed successfully");
    }
}
