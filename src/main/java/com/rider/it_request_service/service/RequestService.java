package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.*;
import com.rider.it_request_service.entity.Category;
import com.rider.it_request_service.entity.Request;
import com.rider.it_request_service.entity.RequestStatusHistory;
import com.rider.it_request_service.entity.User;
import com.rider.it_request_service.exception.GlobalExceptionHandler;
import com.rider.it_request_service.mapper.RequestAdminBoardMapper;
import com.rider.it_request_service.mapper.RequestMapper;
import com.rider.it_request_service.repository.CategoryRepository;
import com.rider.it_request_service.repository.RequestRepository;
import com.rider.it_request_service.repository.RequestStatusHistoryRepository;
import com.rider.it_request_service.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestStatusHistoryRepository requestStatusHistoryRepository;
    @Autowired
    private RequestAdminBoardMapper requestAdminBoardMapper;

    /// AON
    @PersistenceContext
    private EntityManager entityManager;
    ///
    public String generateRequestNumber() {
        // ดึงปี 2 หลัก, เดือน 2 หลัก, วัน 2 หลัก
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // ค้นหาหมายเลขคำขอล่าสุดของวันปัจจุบัน
        Optional<String> lastRequestOpt = requestRepository.findLastRequestNumber("REQ-" + datePart + "-%");

        int nextNumber = 1;
        if (lastRequestOpt.isPresent()) {
            // แยกหมายเลขลำดับสุดท้ายและเพิ่มค่า (+1)
            String lastRequest = lastRequestOpt.get();
            String[] parts = lastRequest.split("-");
            nextNumber = Integer.parseInt(parts[2]) + 1;
        }

        // สร้างหมายเลขคำขอใหม่ (000 -> 001, 002, ...)
        return String.format("REQ-%s-%03d", datePart, nextNumber);
    }

    public List<RequestAdminBoardDTO> getRequestByUserId(int userId){
        userRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("User not found with ID: " + userId));
        List<Request> listRequest = requestRepository.findRequestByuserId(userId);
        return listRequest.stream().map((requestAdminBoardMapper::toDTO)).toList();
    }

    public List<RequestAdminBoardDTO> getRequestByDate(LocalDateTime startDate, LocalDateTime endDate){
        List<Request> listRequest = requestRepository.findByCreatedAtBetween(startDate,endDate);
        return listRequest.stream().map((requestAdminBoardMapper::toDTO)).toList();
    }

    public RequestHistoryAdminBoardDTO getRequestById(int requestid, int userId){
        Request request = requestRepository.findById(requestid)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Request not found with ID: " + requestid));

        // 🔹 ตรวจสอบสิทธิ์: ถ้า userId ไม่ตรงกับเจ้าของคำร้อง ให้โยน Exception
        if (request.getUserId() != userId) {
            throw new GlobalExceptionHandler.UnauthorizedAccessException("You are not authorized to view this request.");
        }

        return requestAdminBoardMapper.toDTOHistory(request);
    }

    public List<RequestDTO> getAllRequest(){
        List<Request> listRequest = requestRepository.findAll();
        return listRequest.stream().map((RequestMapper.INSTANCE::toDTO)).toList();
    }

    public RequestDTO addRequest(CustomUserDetails user, RequestDTO requestDTO) {
        // ตรวจสอบว่า Category และ User มีอยู่จริงหรือไม่
        categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "Category not found with ID: " + requestDTO.getCategoryId()));

        userRepository.findById(user.getUserId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "User not found with ID: " + user.getUserId()));

        // แปลง DTO เป็น Entity
        Request request = RequestMapper.INSTANCE.toEntity(requestDTO);

        request.setUserId(user.getUserId());

        // สร้างหมายเลขคำขอ (REQ-YYYYMM-XXX)
        String requestNumber = generateRequestNumber();
        request.setRequestNumber(requestNumber);

        // บันทึกข้อมูลลงฐานข้อมูล
        request = requestRepository.save(request);

        // แปลง Entity กลับเป็น DTO และคืนค่า
        return RequestMapper.INSTANCE.toDTO(request);
    }


    public RequestHistoryAdminBoardDTO updateStatus(CustomUserDetails user, RequestStatusHistory requestStatusHistory){

        // ✅ ใช้ user ที่ส่งมาจาก Controller แทนการใช้ SecurityContextHolder
        int userId = user.getUserId(); // ✅ ดึง userId ได้เลย ไม่ต้องแปลง object

        Request request = requestRepository.findById(requestStatusHistory.getRequestId()).orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Request not found with ID: " + requestStatusHistory.getRequestId()));

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        int userId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getUserId();
        } else if (principal instanceof User) {
            userId = ((User) principal).getUserId();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }*/


        List<RequestStatusHistory> refStatusHistory = requestStatusHistoryRepository.findByrequestId(requestStatusHistory.getRequestId());
        if (!refStatusHistory.isEmpty()) {
            requestStatusHistory.setRefStatusHistory(
                    refStatusHistory.get(refStatusHistory.size() - 1).getHistoryId());
        }else{
            requestStatusHistory.setRefStatusHistory(null);
        }

        requestStatusHistory.setChangedBy(userId);
        requestStatusHistory.setChangedAt(LocalDateTime.now());
        requestStatusHistoryRepository.save(requestStatusHistory);

        request.setStatus(requestStatusHistory.getStatus());
        request.setUpdatedAt(LocalDateTime.now());
        return requestAdminBoardMapper.toDTOHistory(requestRepository.save(request));
    }

    public List<RequestDTO> getRequestsToDay() {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Request> listRequest = requestRepository.findRequestsByDateRange(startOfDay, endOfDay);
        return listRequest.stream().map((RequestMapper.INSTANCE::toDTO)).toList();
    }

    public List<RequestDTO> getRequestToWeek() {

        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        List<Request> listRequest = requestRepository.findRequestsByDateRange(startOfWeek, endOfWeek);
        return listRequest.stream().map(RequestMapper.INSTANCE::toDTO).toList();
    }

    public List<RequestDTO> getRequestToMonth() {

        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        List<Request> listRequest = requestRepository.findRequestsByDateRange(startOfMonth, endOfMonth);
        return listRequest.stream().map(RequestMapper.INSTANCE::toDTO).toList();
    }

    public Map<String, Object> getRequestTo() {
        Map<String, Object> counts = new HashMap<>();
        counts.put("toDay", requestRepository.countRequestsToday());
        counts.put("thisWeek", requestRepository.countRequestsThisWeek());
        counts.put("thisMonth", requestRepository.countRequestsThisMonth());
        counts.put("total", requestRepository.countTotalRequests());
        return counts;
    }

    public Map<String, Object> getRequestStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        statistics.put("totalRequests", requestRepository.count());//คำร้องขอทั้งหมด
        statistics.put("pendingRequests", requestRepository.countByStatus(Request.Status.PENDING));//คำร้องใหม่
        statistics.put("inProgressRequests", requestRepository.countByStatus(Request.Status.IN_PROGRESS));//ระหว่างดำเนินการ
        statistics.put("resolvedRequests", requestRepository.countByStatus(Request.Status.RESOLVED));//ดำเนินการเรียบร้อย
        statistics.put("rejectedRequests", requestRepository.countByStatus(Request.Status.REJECTED));//ปฏิเสธคำร้อง

        return statistics;
    }

    public Map<String, Object> getInfoUser() {
        Map<String, Object> infoUser = new HashMap<>();
        infoUser.put("totalUser", requestRepository.countUser());
        infoUser.put("avgRequest", requestRepository.countAvgRequest());
        return infoUser;
    }


///  AON------------------------------------------------------

//public List<RequestAdminBoardDTO> searchRequests(SearchRequestDTO searchDto) {
//    // สร้าง CriteriaBuilder
//    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//    CriteriaQuery<Request> query = cb.createQuery(Request.class);
//    Root<Request> root = query.from(Request.class);
//
//    // สร้างเงื่อนไขแบบ Dynamic
//    List<Predicate> predicates = new ArrayList<>();
//
//    if (searchDto.requestId() != null) {
//        predicates.add(cb.equal(root.get("requestId"), searchDto.requestId()));
//    }
//
//    if (searchDto.name() != null) {
//        // สร้าง Subquery สำหรับค้นหา userId จาก User ที่มีชื่อตรงกับ searchDto.name()
//        Subquery<Integer> subquery = query.subquery(Integer.class);
//        Root<User> userRoot = subquery.from(User.class);
//        subquery.select(userRoot.get("userId"));
//        subquery.where(cb.like(cb.lower(userRoot.get("name")), "%" + searchDto.name().toLowerCase() + "%"));
//
//        // ใช้ subquery ในเงื่อนไขของ Request
//        predicates.add(cb.in(root.get("userId")).value(subquery));
//    }
//
//    if (searchDto.requestNumber() != null) {
//        predicates.add(cb.like(root.get("requestNumber"),"%"+ searchDto.requestNumber()+"%"));
//    }
//
//    if (searchDto.categoryId() != null) {
//        //Join<Request, Category> categoryJoin = root.join("categoryId");
    ////        predicates.add(cb.like(cb.lower(categoryJoin.get("categoryId")), "%" + searchDto.categoryId().toLowerCase() + "%"));
//        predicates.add(cb.equal(root.get("categoryId"), searchDto.categoryId()));
//    }
//
//    if (searchDto.status() != null) {
//        predicates.add(cb.equal(root.get("status"), searchDto.status())); // ใช้ Enum ตรงๆ
//    }
//
//    // รวมเงื่อนไขทั้งหมด
//    query.where(predicates.toArray(new Predicate[0]));
//
//    if (!List.of("createdAt", "updatedAt").contains(searchDto.sortBy())) {
//        throw new IllegalArgumentException("Invalid sortBy parameter: " + searchDto.sortBy());
//    }
//
//    // เพิ่มการ Sort
//    if ("asc".equalsIgnoreCase(searchDto.sortDirection())) {
//        query.orderBy(cb.asc(root.get(searchDto.sortBy())));
//    } else {
//        query.orderBy(cb.desc(root.get(searchDto.sortBy())));
//    }
//
//    // ใช้ EntityManager ในการ Query
//    TypedQuery<Request> typedQuery = entityManager.createQuery(query);
//    List<Request> resultList = typedQuery.getResultList();
//
//    // จัดการ Pagination
//    int start = searchDto.page() * searchDto.size();
//    int end = Math.min(start + searchDto.size(), resultList.size());
//    List<Request> pagedList = resultList.subList(start, end);
//
//
//    return resultList.stream()
//            .map(requestAdminBoardMapper::toDTO) // ใช้ instance ของ class
//            .collect(Collectors.toList());
//
//}


    public Page<RequestAdminBoardDTO> searchRequests(SearchRequestDTO searchDto) {
        // สร้าง CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = cb.createQuery(Request.class);
        Root<Request> root = query.from(Request.class);

        // สร้างเงื่อนไขแบบ Dynamic
        List<Predicate> predicates = new ArrayList<>();

        if (searchDto.requestId() != null) {
            predicates.add(cb.equal(root.get("requestId"), searchDto.requestId()));
        }

        if (searchDto.name() != null) {
            // สร้าง Subquery สำหรับค้นหา userId จาก User ที่มีชื่อตรงกับ searchDto.name()
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<User> userRoot = subquery.from(User.class);
            subquery.select(userRoot.get("userId"));
            subquery.where(cb.like(cb.lower(userRoot.get("name")), "%" + searchDto.name().toLowerCase() + "%"));

            // ใช้ subquery ในเงื่อนไขของ Request
            predicates.add(cb.in(root.get("userId")).value(subquery));
        }

        if (searchDto.requestNumber() != null) {
            predicates.add(cb.like(root.get("requestNumber"), "%" + searchDto.requestNumber() + "%"));
        }

        if (searchDto.categoryId() != null) {
            predicates.add(cb.equal(root.get("categoryId"), searchDto.categoryId()));
        }

        if (searchDto.status() != null) {
            predicates.add(cb.equal(root.get("status"), searchDto.status()));
        }

        // รวมเงื่อนไขทั้งหมด
        query.where(predicates.toArray(new Predicate[0]));

        // ตรวจสอบว่า sortBy parameter ที่ส่งมาถูกต้อง
        if (!List.of("createdAt", "updatedAt").contains(searchDto.sortBy())) {
            throw new IllegalArgumentException("Invalid sortBy parameter: " + searchDto.sortBy());
        }

        // เพิ่มการ Sort
        if ("asc".equalsIgnoreCase(searchDto.sortDirection())) {
            query.orderBy(cb.asc(root.get(searchDto.sortBy())));
        } else {
            query.orderBy(cb.desc(root.get(searchDto.sortBy())));
        }


        // ใช้ นับtotalelement ตามคิวรี่่
        String baseQuery = "SELECT COUNT(r) FROM Request r WHERE 1=1 ";

        if (searchDto.requestId() != null) {
            baseQuery += " AND r.requestId = :requestId";
        }
        if (searchDto.name() != null) {
            baseQuery += " AND r.userId IN (SELECT u.userId FROM User u WHERE LOWER(u.name) LIKE :name)";
        }
        if (searchDto.requestNumber() != null) {
            baseQuery += " AND r.requestNumber LIKE :requestNumber";
        }
        if (searchDto.categoryId() != null) {
            baseQuery += " AND r.categoryId = :categoryId";
        }
        if (searchDto.status() != null) {
            baseQuery += " AND r.status = :status";
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(baseQuery, Long.class);

        if (searchDto.requestId() != null) {
            countQuery.setParameter("requestId", searchDto.requestId());
        }
        if (searchDto.name() != null) {
            countQuery.setParameter("name", "%" + searchDto.name().toLowerCase() + "%");
        }
        if (searchDto.requestNumber() != null) {
            countQuery.setParameter("requestNumber", "%" + searchDto.requestNumber() + "%");
        }
        if (searchDto.categoryId() != null) {
            countQuery.setParameter("categoryId", searchDto.categoryId());
        }
        if (searchDto.status() != null) {
            countQuery.setParameter("status", searchDto.status());
        }

        long totalCount = countQuery.getSingleResult();

        // สิ้นสุด นับtotalelement ตามคิวรี่่



        // ใช้ EntityManager ในการ Query
        TypedQuery<Request> typedQuery = entityManager.createQuery(query);
        // คำนวณ pagination
        int page = searchDto.page();
        int size = searchDto.size();
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        List<Request> resultList = typedQuery.getResultList();

        // เปลี่ยนผลลัพธ์ที่ได้เป็น DTO
        List<RequestAdminBoardDTO> dtoList = resultList.stream()
                .map(requestAdminBoardMapper::toDTO)
                .collect(Collectors.toList());

        // ส่งผลลัพธ์กลับในรูปแบบของ Page โดยใช้ totalCount
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }




    public Page<RequestAdminBoardDTO> getRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return requestRepository.findAll(pageable).map(requestAdminBoardMapper::toDTO);
    }

    public List<RequestDTO> getRequestsByStatus(Request.Status status) {
        List<Request> requests = requestRepository.findByStatus(status);

        // แปลง Entity เป็น DTO
        return requests.stream()
                .map(RequestMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

    }

    /*@Transactional
    public void updateRequestStatus(int requestId, Request.Status newStatus, String note,  int userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with ID: " + requestId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // อัปเดตสถานะใน Request
        request.setStatus(newStatus);
        request.setUpdatedAt(LocalDateTime.now());

        // บันทึกใน RequestStatusHistory
        RequestStatusHistory history = RequestStatusHistory.builder()
                .requestId(requestId)
                .status(newStatus)
                .note(note)
                .changedBy(userId) // อาจเป็น userId ของผู้ที่เปลี่ยนสถานะ
                .changedAt(LocalDateTime.now())
                .build();

        requestRepository.save(request);
    }*/

}