package com.rider.it_request_service.repository;

import com.rider.it_request_service.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByname(String name);
}
