package com.app_bank.app_bank.services.impl;

import com.app_bank.app_bank.dto.BankResponse;
import com.app_bank.app_bank.dto.UserRequest;

public interface UserService {

   BankResponse createAccount(UserRequest userRequest);
}
