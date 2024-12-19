package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.*;

public interface UserService {

   BankResponse createAccount(UserRequest userRequest);

   BankResponse balanceEnquiry(EnquiryRequest request);

   String nameEnquiry(EnquiryRequest request);

   BankResponse creditAccount(CreditDebitRequest request);

   BankResponse debitAccount(CreditDebitRequest request);

   BankResponse TransfertBalance(TransfertRequest request);

   BankResponse login(LoginDto loginDto);
}
