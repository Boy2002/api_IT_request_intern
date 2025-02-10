package com.rider.it_request.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    // เก็บในโฟลเดอร์ "uploads" ภายในโปรเจค
    // กำหนดโฟลเดอร์เก็บไฟล์

    public String storeFile(MultipartFile file) {
        try {
            // สร้างโฟลเดอร์ถ้ายังไม่มี
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ตั้งชื่อไฟล์ใหม่ป้องกันชื่อซ้ำ
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // บันทึกไฟล์ลงเครื่อง
            file.transferTo(filePath.toFile());

           // return filePath.toString(); // คืนค่า path ของไฟล์
            return fileName; // คืนค่า path ของไฟล์
        } catch (IOException e) {
            throw new RuntimeException("Error storing file: " + e.getMessage());
        }
    }
}
