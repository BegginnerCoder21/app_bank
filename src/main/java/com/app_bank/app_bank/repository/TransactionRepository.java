package com.app_bank.app_bank.repository;

import com.app_bank.app_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByAccountNumberAndCreateAtBetween(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
}
