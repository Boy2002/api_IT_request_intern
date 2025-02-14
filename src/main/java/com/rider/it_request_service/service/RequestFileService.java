package com.rider.it_request_service.service;

import com.rider.it_request_service.entity.RequestFile;
import com.rider.it_request_service.repository.RequestFileRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RequestFileService {

    private final RequestFileRepository requestFileRepository;

    public RequestFileService(RequestFileRepository requestFileRepository) {
        this.requestFileRepository = requestFileRepository;
    }

    public RequestFile saveFile(MultipartFile file, String filePath, int requestId) {

        RequestFile requestFile =
                RequestFile.builder()
                        .fileName(file.getOriginalFilename())
                        .filePath(filePath)
                        .uploadedAt(LocalDateTime.now())
                        .requestId(requestId)
                        .build();

        return requestFileRepository.save(requestFile);
    }
}
