package com.rider.it_request.service;

import com.rider.it_request.entity.RequestFile;
import com.rider.it_request.repository.RequestFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class RequestFileService {

    private final RequestFileRepository requestFileRepository;

    public RequestFileService(RequestFileRepository requestFileRepository) {
        this.requestFileRepository = requestFileRepository;
    }

    public RequestFile saveFile(MultipartFile file, String filePath,int requestId) {

        RequestFile requestFile = RequestFile.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .uploadedAt(LocalDateTime.now())
                .requestId(requestId)
                .build();

        return requestFileRepository.save(requestFile);
    }
}
