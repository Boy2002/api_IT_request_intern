package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.Request;
import com.rider.it_request_service.entity.RequestFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestFileRepository extends JpaRepository<RequestFile, Integer> {
    List<RequestFile> findRequestFileByrequestId(int id);
}
