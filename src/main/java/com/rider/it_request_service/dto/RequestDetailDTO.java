package com.rider.it_request_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // ใช้แทนการสร้าง Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // สร้าง constructor ที่ไม่มีพารามิเตอร์
@AllArgsConstructor // สร้าง constructor ที่มีพารามิเตอร์ทั้งหมด
public class RequestDetailDTO {

    private int requestId;

    @NotNull(message = "User is required") private String name; // ใช้ id ของ User แทนการใช้ User object

    @NotNull(message = "Category is required") private String category; // ใช้ id ของ Category แทนการใช้ Category object

    @NotBlank(message = "Request Detail is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestDetail;

    @NotBlank(message = "Request Purpose is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestPurpose;

    @NotBlank(message = "Request Specification is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestSpecification;

    private String status; // ใช้ String แทน Enum เพื่อความสะดวก

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String requestNumber;
}
