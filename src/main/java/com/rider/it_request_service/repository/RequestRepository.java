package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("SELECT r.requestNumber FROM Request r WHERE r.requestNumber LIKE :pattern ORDER BY r.requestNumber DESC LIMIT 1")
    Optional<String> findLastRequestNumber(String pattern);


    //List<Request> findRequestByuserId(int userid);
    @Query("SELECT r FROM Request r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<Request> findRequestByuserId(@Param("userId") int userId);

    // เมธอดใหม่สำหรับค้นหาจากช่วงเวลา createdAt
    List<Request> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Request> findNotTimeByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Request r WHERE r.createdAt >= :startDate AND r.createdAt < :endDate")
    List<Request> findRequestsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByStatus(Request.Status status);

    // คำขอที่สร้างในวันนี้
    @Query("SELECT COUNT(r) FROM Request r WHERE r.createdAt = CURRENT_DATE")
    long countRequestsToday();

    // คำขอที่สร้างในสัปดาห์นี้
    @Query("SELECT COUNT(r) FROM Request r WHERE WEEK(r.createdAt) = WEEK(CURRENT_DATE) AND YEAR(r.createdAt) = YEAR(CURRENT_DATE)")
    long countRequestsThisWeek();

    // คำขอที่สร้างในเดือนนี้
    @Query("SELECT COUNT(r) FROM Request r WHERE MONTH(r.createdAt) = MONTH(CURRENT_DATE) AND YEAR(r.createdAt) = YEAR(CURRENT_DATE)")
    long countRequestsThisMonth();

    // คำขอทั้งหมด
    @Query("SELECT COUNT(r) FROM Request r")
    long countTotalRequests();

    @Query("SELECT COUNT(DISTINCT r.userId) FROM Request r")
    long countUser();

    @Query("SELECT AVG(requestCount) FROM (SELECT COUNT(r) AS requestCount FROM Request r GROUP BY r.userId) AS requestCounts")
    double countAvgRequest();


    List<Request> findByStatus(Request.Status status);

}
