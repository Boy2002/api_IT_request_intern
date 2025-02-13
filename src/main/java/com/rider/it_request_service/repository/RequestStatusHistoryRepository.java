package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.Request;
import com.rider.it_request_service.entity.RequestStatusHistory;
import com.rider.it_request_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestStatusHistoryRepository extends JpaRepository<RequestStatusHistory, Integer> {

    List<RequestStatusHistory> findByrequestId(int id);
}
