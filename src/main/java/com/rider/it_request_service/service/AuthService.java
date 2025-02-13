package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.CustomUserDetails;
import com.rider.it_request_service.security.JwtUtil;
import com.rider.it_request_service.security.PasswordUtil;
import com.rider.it_request_service.dto.UserDTO;
import com.rider.it_request_service.entity.User;
import com.rider.it_request_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public String login(String username, String password) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        if (!PasswordUtil.verifyPassword(password, userDetails.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String role = userDetails.getRole();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getRole());

        return jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUserId(), role, claims);
    }

    public void hashPasswords() {
        List<User> users = userRepository.findAll(); // ดึงผู้ใช้ทั้งหมดจากฐานข้อมูล

        users.forEach(user -> {
            // ตรวจสอบว่ารหัสผ่านยังไม่ได้แฮช (ไม่ขึ้นต้นด้วย "$2a$")
            if (!user.getPassword().startsWith("$2a$")) {
                String hashedPassword = PasswordUtil.hashPassword(user.getPassword()); // แฮชรหัสผ่าน
                user.setPassword(hashedPassword); // อัปเดตรหัสผ่าน
            }
        });

        userRepository.saveAll(users); // บันทึกกลับเข้าไปในฐานข้อมูล
    }
}

