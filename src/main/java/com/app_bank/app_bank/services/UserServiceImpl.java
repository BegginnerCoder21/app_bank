package com.app_bank.app_bank.services.impl;

import com.app_bank.app_bank.dto.AccountInfo;
import com.app_bank.app_bank.dto.BankResponse;
import com.app_bank.app_bank.dto.UserRequest;
import com.app_bank.app_bank.entity.User;
import com.app_bank.app_bank.repository.UserRepository;
import com.app_bank.app_bank.utils.AccountUtils;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService{

    private UserRepository userRespository;

    public UserServiceImpl(UserRepository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (this.userRespository.existsByEmail(userRequest.getEmail())){

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIF")
                .build();

        User saveUser = this.userRespository.save(newUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(saveUser.getFirstname() + " " + saveUser.getLastname() + " " + saveUser.getOtherName())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountBalance(saveUser.getAccountBalance())
                        .build())
                .build();
    }
}
