package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.RequestStatusHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusHistoryRepository
        extends JpaRepository<RequestStatusHistory, Integer> {

    List<RequestStatusHistory> findByrequestId(int id);
}
