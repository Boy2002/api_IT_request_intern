package com.rider.it_request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rider.it_request.dto.RequestAdminBoardDTO;
import com.rider.it_request.dto.RequestDTO;
import com.rider.it_request.dto.RequestHistoryAdminBoardDTO;
import com.rider.it_request.dto.SearchRequestDTO;
import com.rider.it_request.entity.Request;
import com.rider.it_request.entity.RequestFile;
import com.rider.it_request.entity.RequestStatusHistory;
import com.rider.it_request.mapper.RequestAdminBoardMapper;
import com.rider.it_request.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/requests")
@Tag(name = "requests API", description = "ตัวอย่าง API สำหรับ Swagger")
public class RequestController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private ReportExportService reportExportService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private RequestFileService requestFileService;

    @Autowired
    private RequestAdminBoardMapper requestAdminBoardMapper;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> Track_ViewRequestFromHistory(@PathVariable("id") int userid) {
        List<RequestAdminBoardDTO> requests = requestService.getRequestByUserId(userid);
        if (requests.isEmpty()) {
            return new ResponseEntity<>("No requests found for user ID: " + userid, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@PathVariable("id") int requestId) {
        RequestHistoryAdminBoardDTO requests = requestService.getRequestById(requestId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }


//    @PreAuthorize("hasRole('EMPLOYEE')")
//    @PostMapping
//    public ResponseEntity<Object> RecordRequestFrom(@Valid @RequestBody RequestDTO RequestDTO, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//
//            List<String> errorMessages = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)  // ดึงข้อความ default จาก error
//                    .collect(Collectors.toList());
//
//            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
//        }
//
//        RequestDTO savedRequestDTO = requestService.addRequest(RequestDTO);
//        return new ResponseEntity<>(savedRequestDTO, HttpStatus.CREATED);
//    }

//    @PreAuthorize("hasRole('EMPLOYEE')")
//    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.MULTIPART_FORM_DATA_VALUE},
//    produces = MediaType.APPLICATION_JSON_VALUE)
//
//    public ResponseEntity<Object> RecordRequestForm(
//            @RequestPart(value = "request", required = true) @Valid RequestDTO requestDTO,
//            @RequestPart("files") MultipartFile[] files, // เปลี่ยนเป็น Array
//            BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            List<String> errorMessages = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
//        }
//
//        // 1. บันทึกข้อมูล Request
//        RequestDTO savedRequestDTO = requestService.addRequest(requestDTO);
//
//        // 2. วนลูปเพื่อบันทึกไฟล์ทั้งหมด
//        for (MultipartFile file : files) {
//            if (file.isEmpty()) continue;
//            String filePath = fileStorageService.storeFile(file);
//            RequestFile savedFile = requestFileService.saveFile(file, filePath, savedRequestDTO.getRequestId());
//        }
//
//        RequestAdminBoardDTO requestAdminBoardDTO = requestAdminBoardMapper.RequestDTOtoDTO(savedRequestDTO);
//
//        return new ResponseEntity<>(requestAdminBoardDTO, HttpStatus.CREATED);
//    }



    @PreAuthorize("hasRole('EMPLOYEE') and hasRole('ADMIN')")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> RecordRequestForm(
            @RequestPart(value = "request", required = true) String jsonObj,
            @RequestPart("files") MultipartFile[] files,
            BindingResult bindingResult) {

        try {
            // แปลง JSON String เป็น RequestDTO
            ObjectMapper objectMapper = new ObjectMapper();
            RequestDTO requestDTO = objectMapper.readValue(jsonObj, RequestDTO.class);

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
            }

            // 1. บันทึกข้อมูล Request
            RequestDTO savedRequestDTO = requestService.addRequest(requestDTO);

            // 2. วนลูปเพื่อบันทึกไฟล์ทั้งหมด
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String filePath = fileStorageService.storeFile(file);
                RequestFile savedFile = requestFileService.saveFile(file, filePath, savedRequestDTO.getRequestId());
            }

            RequestAdminBoardDTO requestAdminBoardDTO = requestAdminBoardMapper.RequestDTOtoDTO(savedRequestDTO);

            return new ResponseEntity<>(requestAdminBoardDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("เกิดข้อผิดพลาดในการแปลง JSON: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }






}

//เเบ่งระหว่าง employee กับ admin ------------------------------------------------------------------------------------------------------
@RestController
@RequestMapping("/api/req-admin")
class AdminController {


    @Autowired
    private RequestService requestService;
    @Autowired
    private ReportExportService reportExportService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update_status")
    public ResponseEntity<Object> updateDocumentStatus(@Valid @RequestBody RequestStatusHistory requestStatusHistory, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        RequestHistoryAdminBoardDTO updateStatusRequest = requestService.updateStatus(requestStatusHistory);
        return new ResponseEntity<>(updateStatusRequest, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export/user/{id}")
    public ResponseEntity<byte[]> exportAllReportById(@PathVariable("id") int userId) throws IOException {
        List<RequestAdminBoardDTO> requests = requestService.getRequestByUserId(userId);

        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("No requests found for user ID: " + userId).getBytes());
        }

        // เรียกใช้บริการเพื่อสร้างไฟล์ Excel
        byte[] excelData = reportExportService.exportToExcel(requests);

        // ตั้งค่า Header สำหรับดาวน์โหลดไฟล์
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=RequestReport.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
    }

//    @GetMapping("/export/{id}")
//    public ResponseEntity<Object> exportReportById(@PathVariable("id") int requestId) throws IOException {
//        RequestDTO requests = requestService.getRequestById(requestId);
//
//        // เรียกใช้บริการเพื่อสร้างไฟล์ Excel
//        byte[] excelData = reportExportService.exportToExcel(requests);
//
//        // ตั้งค่า Header สำหรับดาวน์โหลดไฟล์
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=RequestReport.xlsx")
//                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//                .body(excelData);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export/by-date-range")
    public ResponseEntity<Object> getRequestsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) throws IOException {

        LocalDateTime start = LocalDateTime.parse(startDate); // ควรมีการจัดการ Format ของวันที่
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<RequestAdminBoardDTO> requests = requestService.getRequestByDate(start,end);

        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("No requests found: " ).getBytes());
        }

        // เรียกใช้บริการเพื่อสร้างไฟล์ Excel
        byte[] excelData = reportExportService.exportToExcel(requests);

        // ตั้งค่า Header สำหรับดาวน์โหลดไฟล์
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=RequestReport.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/export")
    public ResponseEntity<Object> exportToExcel(@RequestBody List<RequestAdminBoardDTO> requests) throws IOException {

        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No requests found.".getBytes());
        }

        // เรียกใช้บริการเพื่อสร้างไฟล์ Excel
        byte[] excelData = reportExportService.exportToExcel(requests);

        // ตั้งค่า Header สำหรับดาวน์โหลดไฟล์
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=RequestReport.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/to-day")
    public ResponseEntity<Object> DashBoard_AllRequestDay() {
        List<RequestDTO> requests = requestService.getRequestsToDay();
        if (requests.isEmpty()) {
            return new ResponseEntity<>("No requests found ", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/to-week")
    public ResponseEntity<Object> DashBoard_AllRequestWeek() {
        List<RequestDTO> requests = requestService.getRequestToWeek();
        if (requests.isEmpty()) {
            return new ResponseEntity<>("No requests found ", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/to-month")
    public ResponseEntity<Object> DashBoard_AllRequestMonth() {
        List<RequestDTO> requests = requestService.getRequestToMonth();
        if (requests.isEmpty()) {
            return new ResponseEntity<>("No requests found ", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/request-to")
    public ResponseEntity<Map<String, Object>> getRequestTo() {
        Map<String, Object> date = requestService.getRequestTo();
        return new ResponseEntity<>(date, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getRequestStatistics() {
        Map<String, Object> statistics = requestService.getRequestStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info-user")
    public ResponseEntity<Map<String, Object>> getRequestInfoUser() {
        Map<String, Object> infoUser = requestService.getInfoUser();
        return new ResponseEntity<>(infoUser, HttpStatus.OK);
    }

    //    AON---------------------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<RequestAdminBoardDTO>> searchRequests(
            @RequestParam(value = "requestId", required = false) Integer requestId,
            @RequestParam(value = "requestNumber", required = false) String requestNumber,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "status", required = false) Request.Status status,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        // สร้าง SearchRequestDTO
        SearchRequestDTO searchRequestDTO = new SearchRequestDTO(
                requestId,
                requestNumber,
                name,
                categoryId,
                status,
                sortBy,
                sortDirection,
                page,
                size
        );

        // เรียกใช้งาน service
        List<RequestAdminBoardDTO> results = requestService.searchRequests(searchRequestDTO);

        return ResponseEntity.ok(results);
    }

    //สำหรับดึงคำร้องเเต่ละสถานะ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequestDTO>> getRequestsByStatus(@PathVariable Request.Status status) {
        List<RequestDTO> requests = requestService.getRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    //ดึงสถานะทั้งหมดสำหรับ drop down
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getAllStatuses() {
        List<String> statuses = Arrays.stream(Request.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    //อัพเดทสภานะ
   /* @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{requestId}/status")
    public ResponseEntity<String> updateRequestStatus(
            @PathVariable int requestId,
            @RequestBody Map<String, String> requestBody,
            @PathVariable int userId) {
        String newStatus = requestBody.get("status");
        String note = requestBody.get("note");
        requestService.updateRequestStatus(requestId, Request.Status.valueOf(newStatus), note,userId);
        return ResponseEntity.ok("Status updated successfully");
    }*/

    //เเบ่งหน้าเเบบทั่วไปสำหรับข้อมูลทั้งหมด
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RequestAdminBoardDTO>> getRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RequestAdminBoardDTO> requests = requestService.getRequests(page, size);
        return ResponseEntity.ok(requests);
    }
}
