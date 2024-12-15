package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.TransactionDto;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
