package com.rider.it_request_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")//ตารางประเภทคำขอ
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @NotNull(message = "Category name cannot be null")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // ค่าเริ่มต้นคือ false (ยังไม่ถูกลบ)

}
