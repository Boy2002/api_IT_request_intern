package com.rider.it_request.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_status_history")//ตารางเก็บข้อมูลการเปลี่ยนสถานะ
public class RequestStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @NotNull(message = "Request ID cannot be null")
    @Column(name = "request_id", nullable = false)
    private int requestId;

    @Column(name = "changed_by", nullable = false)
    private int changedBy;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Request.Status status;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "ref_status_history",nullable = true)
    private Integer refStatusHistory; // ใช้ Integer แทน int เพื่อรองรับค่า null


}
