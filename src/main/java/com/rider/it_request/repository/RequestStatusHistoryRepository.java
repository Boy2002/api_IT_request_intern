package com.rider.it_request.repository;

import com.rider.it_request.entity.Request;
import com.rider.it_request.entity.RequestStatusHistory;
import com.rider.it_request.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestStatusHistoryRepository extends JpaRepository<RequestStatusHistory, Integer> {

    List<RequestStatusHistory> findByrequestId(int id);
}
