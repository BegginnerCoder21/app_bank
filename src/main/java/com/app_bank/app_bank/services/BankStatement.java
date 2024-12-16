package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.StatementRequest;
import com.app_bank.app_bank.entity.Transaction;
import com.app_bank.app_bank.repository.TransactionRepository;
import com.app_bank.app_bank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatement {


    private TransactionRepository transactionRepository;

    public List generateStatement(StatementRequest statementRequest)

    {

        LocalDateTime startDate = LocalDate.parse(statementRequest.getStartDate(), DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(statementRequest.getEndDate(), DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);

        return this.transactionRepository.findByAccountNumberAndCreateAtBetween(statementRequest.getAccountNumber(), startDate, endDate);
    }
}
