package com.rider.it_request_service.security;

import com.rider.it_request_service.repository.UserRepository;
import com.rider.it_request_service.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/api/auth/login", "/api/auth/hash-passwords")
                                        .permitAll()
                                        .requestMatchers("/api/req-admin/**", "/api/categories/**")
                                        .permitAll() // .hasRole("ADMIN")
                                        .requestMatchers("/api/requests/**")
                                        .permitAll() // .hasAnyRole("EMPLOYEE",
                                        // "ADMIN")//.permitAll()
                                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                                        .permitAll()
                                        .requestMatchers("/api/files/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //    @Bean
    //    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //        http
    //                // ปิด CSRF (เฉพาะสำหรับการพัฒนา)
    //                .csrf(csrf -> csrf.disable())
    //
    //                // การอนุญาตการเข้าถึง
    //                .authorizeHttpRequests(authz -> authz
    //                        .requestMatchers("/requests/**").permitAll()  // อนุญาตให้เข้าถึง
    // /requests ได้ทั้งหมด
    //
    //                        .anyRequest().permitAll()//.authenticated()  //
    // ต้องการการยืนยันตัวตนสำหรับ request อื่น ๆ
    //                )
    //
    //                // ปิดการใช้งานฟอร์มล็อกอินเริ่มต้น
    //                .formLogin(formLogin -> formLogin.disable())
    //
    //                // ปิดการใช้งาน HTTP Basic Authentication
    //                .httpBasic(httpBasic -> httpBasic.disable());
    //
    //        return http.build();
    //    }
}
