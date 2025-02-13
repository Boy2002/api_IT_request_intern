package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.RequestAdminBoardDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ReportExportService {

    public byte[] exportToExcel(List<RequestAdminBoardDTO> requestList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Request Report");

        // สร้าง header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"วันที่ร้องขอ", "หมายเลขรายการเอกสาร", "ชื่อผู้ร้องขอ", "ตำแหน่ง", "ประเภทคำร้อง", "รายละเอียด", "เหตุผล", "มาตรฐานหรือสเปคที่ต้องการ", "สถานะ"};

        // สร้าง CellStyle สำหรับหัวตาราง
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);  // ทำให้ฟอนต์หนา
        headerFont.setFontHeightInPoints((short) 12);  // ขนาดฟอนต์
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // ตั้งสีพื้นหลัง
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // ให้สีพื้นหลังสม่ำเสมอ

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);  // ใช้ CellStyle ที่ตั้งไว้
        }

        // กำหนดรูปแบบวันที่ไทย
        SimpleDateFormat thaiDateFormat = new SimpleDateFormat("d MMMM yyyy HH:mm", new Locale("th", "TH"));

        // เพิ่มข้อมูลจาก requestList ลงใน Excel
        int rowNum = 1;
        for (RequestAdminBoardDTO request : requestList) {
            Row row = sheet.createRow(rowNum++);

            // แปลงวันที่เป็นรูปแบบไทย
            String formattedDate = "";
            try {
                if (request.getCreatedAt() != null) {
                    // ตรวจสอบว่าค่า createdAt เป็นประเภท LocalDateTime หรือไม่
                    LocalDateTime createdAtDateTime = request.getCreatedAt();
                    if (createdAtDateTime != null) {
                        // แปลง LocalDateTime เป็น String โดยใช้ DateTimeFormatter
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String dateString = createdAtDateTime.format(formatter);

                        // แปลงเป็น Date object สำหรับใช้ใน SimpleDateFormat
                        Date createdAtDate = java.sql.Timestamp.valueOf(dateString);

                        // แปลงวันที่เป็นรูปแบบที่ต้องการ
                        formattedDate = thaiDateFormat.format(createdAtDate);
                    }
                }
            } catch (Exception e) {
                // จับข้อผิดพลาดที่เกิดขึ้นในการแปลงวันที่
                System.out.println("Error formatting date: " + e.getMessage());
            }

            // แปลงสถานะให้แสดงข้อความตามที่กำหนด
            String status = request.getStatus();
            String statusText = getStatusText(status);

            row.createCell(0).setCellValue(formattedDate);
            row.createCell(1).setCellValue(request.getRequestNumber());
            row.createCell(2).setCellValue(request.getName());
            row.createCell(3).setCellValue(request.getPosition());
            row.createCell(4).setCellValue(request.getCategoryName());
            row.createCell(5).setCellValue(request.getRequestDetail());
            row.createCell(6).setCellValue(request.getRequestPurpose());
            row.createCell(7).setCellValue(request.getRequestSpecification());
            row.createCell(8).setCellValue(statusText);
        }

        // ทำ Auto Size ให้คอลัมน์ทั้งหมด
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // เขียนข้อมูล Excel ลงใน ByteArray และปิด Workbook
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        } finally {
            workbook.close();
        }
    }

    // ฟังก์ชันแปลงสถานะเป็นข้อความที่ต้องการ
    private String getStatusText(String status) {
        switch (status) {
            case "PENDING":
                return "รอดำเนินการ";
            case "IN_PROGRESS":
                return "กำลังดำเนินการ";
            case "RESOLVED":
                return "เสร็จสิ้น";
            case "REJECTED":
                return "ถูกปฏิเสธ";
            default:
                return "ไม่ทราบสถานะ";
        }
    }
}
