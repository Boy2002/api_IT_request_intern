package com.rider.it_request_service.mapper;

import com.rider.it_request_service.dto.RequestAdminBoardDTO;
import com.rider.it_request_service.dto.RequestDTO;
import com.rider.it_request_service.dto.RequestHistoryAdminBoardDTO;
import com.rider.it_request_service.dto.RequestStatusHistoryDTO;
import com.rider.it_request_service.entity.*;
import com.rider.it_request_service.repository.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestAdminBoardMapper {
    @Autowired private UserRepository userRepository;

    @Autowired private CategoryRepository categoryRepository;

    @Autowired private RequestFileRepository requestFileRepository;

    @Autowired private RequestStatusHistoryRepository requestStatusHistoryRepository;

    public RequestAdminBoardDTO toDTO(Request request) {

        Optional<User> user = userRepository.findById(request.getUserId());
        Optional<Category> category = categoryRepository.findById(request.getCategoryId());
        List<RequestFile> requestFile =
                requestFileRepository.findRequestFileByrequestId(request.getRequestId());

        List<String> filePaths =
                requestFile.stream().map(RequestFile::getFilePath).collect(Collectors.toList());

        String files = filePaths.isEmpty() ? "notfound" : String.join(", ", filePaths);

        return new RequestAdminBoardDTO(
                request.getCreatedAt(), // วันที่ร้องขอ
                request.getRequestId(), // หมายเลขคำร้อง
                request.getRequestNumber(),
                user.map(User::getName).orElse("Unknown"), // ชื่อผู้ร้องขอ
                user.map(User::getPosition).orElse("Unknown"), // ชื่อผู้ร้องขอ
                category.map(Category::getCategoryName).orElse("Unknown"), // ประเภทคำร้อง
                request.getRequestDetail(), // รายละเอียดคำร้อง
                request.getRequestPurpose(), // วัตถุประสงค์
                request.getRequestSpecification(), // สเปคที่ต้องการ
                files, // ชื่อไฟล์ หรือ "notfound" ถ้าไม่มี
                request.getStatus().toString() // สถานะ
                );
    }

    public RequestHistoryAdminBoardDTO toDTOHistory(Request request) {

        Optional<User> user = userRepository.findById(request.getUserId());
        Optional<Category> category = categoryRepository.findById(request.getCategoryId());
        List<RequestFile> requestFile =
                requestFileRepository.findRequestFileByrequestId(request.getRequestId());
        List<String> fileNames =
                requestFile.stream().map(RequestFile::getFileName).collect(Collectors.toList());
        String files = fileNames.isEmpty() ? "notfound" : String.join(", ", fileNames);
        List<RequestStatusHistory> historyList =
                requestStatusHistoryRepository.findByrequestId(request.getRequestId());
        /*Optional<User> ChangedBy = userRepository.findById(history.getChangedBy());*/

        List<RequestStatusHistoryDTO> historyDTOs =
                historyList.stream()
                        .map(
                                history ->
                                        new RequestStatusHistoryDTO(
                                                history.getHistoryId(),
                                                history.getRequestId(),
                                                userRepository
                                                        .findById(history.getChangedBy()) // ✅ ดึง
                                                        // user
                                                        // ที่ทำการอัปเดต
                                                        .map(User::getName)
                                                        .orElse("Unknown"),
                                                history.getStatus(),
                                                history.getNote(),
                                                history.getChangedAt(),
                                                history.getRefStatusHistory()))
                        .collect(Collectors.toList());

        return new RequestHistoryAdminBoardDTO(
                request.getCreatedAt(), // วันที่ร้องขอ
                request.getRequestId(), // หมายเลขคำร้อง
                request.getRequestNumber(),
                user.map(User::getName).orElse("Unknown"), // ชื่อผู้ร้องขอ
                category.map(Category::getCategoryName).orElse("Unknown"), // ประเภทคำร้อง
                request.getRequestDetail(), // รายละเอียดคำร้อง
                request.getRequestPurpose(), // วัตถุประสงค์
                request.getRequestSpecification(), // สเปคที่ต้องการ
                files, // ชื่อไฟล์ หรือ "notfound" ถ้าไม่มี
                request.getStatus().toString(), // สถานะ
                historyDTOs);
    }

    public RequestAdminBoardDTO RequestDTOtoDTO(RequestDTO requestDTO) {

        Optional<User> user = userRepository.findById(requestDTO.getUserId());
        Optional<Category> category = categoryRepository.findById(requestDTO.getCategoryId());
        List<RequestFile> requestFile =
                requestFileRepository.findRequestFileByrequestId(requestDTO.getRequestId());

        List<String> fileNames =
                requestFile.stream().map(RequestFile::getFileName).collect(Collectors.toList());

        String files = fileNames.isEmpty() ? "notfound" : String.join(", ", fileNames);

        return new RequestAdminBoardDTO(
                requestDTO.getCreatedAt(), // วันที่ร้องขอ
                requestDTO.getRequestId(), // หมายเลขคำร้อง
                requestDTO.getRequestNumber(),
                user.map(User::getName).orElse("Unknown"), // ชื่อผู้ร้องขอ
                user.map(User::getPosition).orElse("Unknown"),
                category.map(Category::getCategoryName).orElse("Unknown"), // ประเภทคำร้อง
                requestDTO.getRequestDetail(), // รายละเอียดคำร้อง
                requestDTO.getRequestPurpose(), // วัตถุประสงค์
                requestDTO.getRequestSpecification(), // สเปคที่ต้องการ
                files, // ชื่อไฟล์ทั้งหมดที่แยกโดยคอมม่า
                requestDTO.getStatus().toString() // สถานะ
                );
    }
}
