package com.rider.it_request_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int requestId;

    @Column(name = "request_number", unique = true, nullable = false, length = 20)
    private String requestNumber; // หมายเลขคำขอ เช่น REQ-202410-001

    @NotNull(message = "User ID cannot be null") @Positive(message = "User ID must be a positive number") @Column(name = "user_id", nullable = false)
    private int userId;

    @NotNull(message = "Category ID cannot be null") @Positive(message = "Category ID must be a positive number") @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Size(max = 1000, message = "Request detail must be less than 1000 characters")
    @Column(name = "request_detail", columnDefinition = "TEXT")
    private String requestDetail;

    @Size(max = 1000, message = "Request purpose must be less than 1000 characters")
    @Column(name = "request_purpose", columnDefinition = "TEXT")
    private String requestPurpose;

    @Size(max = 1000, message = "Request specification must be less than 1000 characters")
    @Column(name = "request_specification", columnDefinition = "TEXT")
    private String requestSpecification;

    @NotNull(message = "Status cannot be null") @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDING;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enum for Status
    public enum Status {
        PENDING,
        IN_PROGRESS,
        RESOLVED,
        REJECTED
    }
}
