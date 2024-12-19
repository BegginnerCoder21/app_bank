package com.app_bank.app_bank.repository;

import com.app_bank.app_bank.dto.EnquiryRequest;
import com.app_bank.app_bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
    Optional<User> findByEmail(String email);
}
