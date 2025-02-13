package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.RequestAdminBoardDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ReportExportService {

    public byte[] exportToExcel(List<RequestAdminBoardDTO> requestList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Request Report");

        // สร้าง header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {
            "Created At",
            "Request Number",
            "User",
            "Position",
            "Category",
            "requestDetail",
            "requestPurpose",
            "requestSpecification",
            "fileName",
            "status"
        };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก requestList ลงใน Excel
        int rowNum = 1;
        for (RequestAdminBoardDTO request : requestList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(request.getCreatedAt().toString());
            row.createCell(1).setCellValue(request.getRequestNumber());
            row.createCell(2).setCellValue(request.getName());
            row.createCell(3).setCellValue(request.getPosition());
            row.createCell(4).setCellValue(request.getCategoryName());
            row.createCell(5).setCellValue(request.getRequestDetail());
            row.createCell(6).setCellValue(request.getRequestPurpose());
            row.createCell(7).setCellValue(request.getRequestSpecification());
            row.createCell(8).setCellValue(request.getFileName());
            row.createCell(9).setCellValue(request.getStatus());
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
}
