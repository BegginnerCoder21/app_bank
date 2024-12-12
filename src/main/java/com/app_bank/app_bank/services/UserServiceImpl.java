package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.*;
import com.app_bank.app_bank.entity.User;
import com.app_bank.app_bank.repository.UserRepository;
import com.app_bank.app_bank.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRespository;
    private EmailService emailService;

    public UserServiceImpl(EmailService emailService, UserRepository userRespository) {
        this.emailService = emailService;
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

        String fullNameUser = AccountUtils.fullNameUser(saveUser);
        this.emailService.sendMailAlert(EmailDetails.builder()
                        .recipient(saveUser.getEmail())
                        .subject(AccountUtils.ACCOUNT_CREATION_MAIL_SUBJECT)
                        .mailBody("Félicitatiion ! Votre compte a été crée avec succès \nLes Détails Du Compte: \n" +
                                "Nom Du Compte: " + fullNameUser + "\nNumero Du Compte: " + saveUser.getAccountNumber())
                        .build());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(fullNameUser)
                        .accountNumber(saveUser.getAccountNumber())
                        .accountBalance(saveUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {

        boolean isAccountExist = this.userRespository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExist)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NO_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NO_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userFound = this.userRespository.findByAccountNumber(request.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userFound.getAccountNumber())
                        .accountName(AccountUtils.fullNameUser(userFound))
                        .accountBalance(userFound.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = this.userRespository.existsByAccountNumber(request.getAccountNumber());
        System.out.println(request.getAccountNumber());
        if(!isAccountExist)
        {
            return AccountUtils.ACCOUNT_NO_EXIST_MESSAGE;
        }

        User userFound = this.userRespository.findByAccountNumber(request.getAccountNumber());

        return AccountUtils.fullNameUser(userFound);
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        return null;
    }
}
