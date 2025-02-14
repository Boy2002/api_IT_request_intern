package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByIsDeletedFalse();
}
