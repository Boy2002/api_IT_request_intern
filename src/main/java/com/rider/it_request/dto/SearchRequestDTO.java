package com.rider.it_request.dto;


import com.rider.it_request.entity.Request;

public record SearchRequestDTO(
        Integer requestId, // หมายเลขคำร้อง
        String requestNumber, // หมายเลขคำร้อง
        String name, // ชื่อผู้ร้องขอ
        Integer categoryId,    // ชื่อ Category
        Request.Status status,          // สถานะ เช่น PENDING, APPROVED
        String sortBy,          // ชื่อฟิลด์ที่ต้องการเรียง
        String sortDirection,   // asc/desc
        int page,               // หน้า
        int size                // จำนวนต่อหน้า
) {}