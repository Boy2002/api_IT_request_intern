package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.UserDTO;
import com.rider.it_request_service.entity.User;
import com.rider.it_request_service.repository.UserRepository;
import com.rider.it_request_service.security.JwtUtil;
import com.rider.it_request_service.security.PasswordUtil;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;

    @Autowired private JwtUtil jwtUtil;

    public String login(UserDTO UserDTO) {
        User user =
                userRepository
                        .findByname(UserDTO.name())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        if (!PasswordUtil.verifyPassword(UserDTO.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String role = user.getRole().name();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return jwtUtil.generateToken(user.getName(), user.getUserId(), role, claims);
    }

    public void hashPasswords() {
        List<User> users = userRepository.findAll(); // ดึงผู้ใช้ทั้งหมดจากฐานข้อมูล

        users.forEach(
                user -> {
                    // ตรวจสอบว่ารหัสผ่านยังไม่ได้แฮช (ไม่ขึ้นต้นด้วย "$2a$")
                    if (!user.getPassword().startsWith("$2a$")) {
                        String hashedPassword =
                                PasswordUtil.hashPassword(user.getPassword()); // แฮชรหัสผ่าน
                        user.setPassword(hashedPassword); // อัปเดตรหัสผ่าน
                    }
                });

        userRepository.saveAll(users); // บันทึกกลับเข้าไปในฐานข้อมูล
    }
}
