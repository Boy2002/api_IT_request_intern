package com.rider.it_request_service.security;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(30));  // จำกัดขนาดไฟล์ 30MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(30)); // จำกัดขนาด request ทั้งหมด 30MB
        return factory.createMultipartConfig();
    }
}
