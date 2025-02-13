package com.rider.it_request_service.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // อนุญาตทุก endpoint
                        .allowedOrigins("*")
                        //                        .allowedOrigins("http://127.0.0.1:5500", // ระบุ
                        // Origin ที่อนุญาต
                        //
                        // "https://4af6-124-120-251-50.ngrok-free.app",
                        //                                "http://localhost:4200") // ระบุ Origin
                        // ที่อนุญาต
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("My API").version("v1"));
    }
}
