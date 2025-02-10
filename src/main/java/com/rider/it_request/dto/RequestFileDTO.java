package com.rider.it_request.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestFileDTO {

    private int fileId;

    @NotNull(message = "Request ID cannot be null")
    @Column(name = "request_id", nullable = false)
    private String requestId;

    @NotNull(message = "File name cannot be null")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    private String fileName;

    @NotNull(message = "File path cannot be null")
    @Size(max = 500, message = "File path cannot exceed 500 characters")
    private String filePath;

    @NotNull(message = "Upload date cannot be null")
    private LocalDateTime uploadedAt;

}
