package com.rider.it_request_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAdminBoardDTO {
    // วันที่ร้องขอ
    private LocalDateTime createdAt;
    // หมายเลขที่ร้องขอ
    private int requestId;

    private String requestNumber;

    // ชื่อผู้ร้องขอ
    @NotNull(message = "User is required") private String name; // ใช้ id ของ User แทนการใช้ User object

    private String position;

    // ประเภทคำร้องขอ
    @NotNull(message = "Category is required") private String categoryName; // ใช้ id ของ Category แทนการใช้ Category object

    // รายละเอียนกคำร้อง
    @NotBlank(message = "Request Detail is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestDetail;

    // วัตถุประสงค์
    @NotBlank(message = "Request Purpose is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestPurpose;

    // ผลลัทธ์ที่ต้องการ
    @NotBlank(message = "Request Specification is required") // ตรวจสอบไม่ให้ค่าว่างหรือช่องว่าง
    private String requestSpecification;

    private String fileName;
    // สถานะ
    private String status; // ใช้ String แทน Enum เพื่อความสะดวก
}
