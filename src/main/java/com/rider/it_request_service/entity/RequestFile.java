package com.rider.it_request_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_files") // ตารางเก็บไฟล์
public class RequestFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;

    @NotNull(message = "Request ID cannot be null") @Column(name = "request_id", nullable = false)
    private int requestId;

    @NotNull(message = "File name cannot be null") @Size(max = 255, message = "File name cannot exceed 255 characters")
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @NotNull(message = "File path cannot be null") @Size(max = 500, message = "File path cannot exceed 500 characters")
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @NotNull(message = "Upload date cannot be null") @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
}
