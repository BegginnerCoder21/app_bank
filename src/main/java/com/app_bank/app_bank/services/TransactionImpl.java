package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.TransactionDto;
import com.app_bank.app_bank.entity.Transaction;
import com.app_bank.app_bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {

        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();

        this.transactionRepository.save(transaction);
    }
}
