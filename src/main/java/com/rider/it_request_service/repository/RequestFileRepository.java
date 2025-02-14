package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.RequestFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestFileRepository extends JpaRepository<RequestFile, Integer> {
    List<RequestFile> findRequestFileByrequestId(int id);
}
