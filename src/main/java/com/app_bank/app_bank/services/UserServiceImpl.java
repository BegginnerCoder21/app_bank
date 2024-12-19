package com.app_bank.app_bank.services;

import com.app_bank.app_bank.config.JwtTokenProvider;
import com.app_bank.app_bank.dto.*;
import com.app_bank.app_bank.entity.User;
import com.app_bank.app_bank.repository.UserRepository;
import com.app_bank.app_bank.utils.AccountUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRespository;
    private final EmailService emailService;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRespository, EmailService emailService, TransactionService transactionService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRespository = userRespository;
        this.emailService = emailService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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
                .password(this.passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .role(userRequest.getRole())
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

    public BankResponse login(LoginDto loginDto)
    {
        System.out.println(loginDto.getEmail());
        System.out.println(loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder()
                .recipient(loginDto.getEmail())
                .subject("Connexion A Votre Compte")
                .mailBody("Une connexion viens d'être effectué sur votre compte. \nS'il ne s'agit pas de vous veillez contacter votre banque le plus tôt possible.")
                .build();

        this.emailService.sendMailAlert(loginAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(this.jwtTokenProvider.generateToken(authentication))
                .accountInfo(null)
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

        User foundUser = this.userRespository.findByAccountNumber(request.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(AccountUtils.fullNameUser(foundUser))
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = this.userRespository.existsByAccountNumber(request.getAccountNumber());
        //System.out.println(request.getAccountNumber());
        if(!isAccountExist)
        {
            return AccountUtils.ACCOUNT_NO_EXIST_MESSAGE;
        }

        User foundUser = this.userRespository.findByAccountNumber(request.getAccountNumber());

        return AccountUtils.fullNameUser(foundUser);
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = this.userRespository.existsByAccountNumber(request.getAccountNumber());

        //System.out.println(request.getAccountNumber());
        if(!isAccountExist)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NO_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NO_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = this.userRespository.findByAccountNumber(request.getAccountNumber());

        foundUser.setAccountBalance(foundUser.getAccountBalance().add(request.getAmount()));
        this.userRespository.save(foundUser);

        this.transactionService.saveTransaction(TransactionDto.builder()
                        .transactionType("CREDITE")
                        .accountNumber(request.getAccountNumber())
                        .amount(request.getAmount())
                        .build());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(AccountUtils.fullNameUser(foundUser))
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    public BankResponse debitAccount(CreditDebitRequest request)
    {
        boolean isAccountExist = this.userRespository.existsByAccountNumber(request.getAccountNumber());

        //System.out.println(request.getAccountNumber());
        if(!isAccountExist)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NO_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NO_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = this.userRespository.findByAccountNumber(request.getAccountNumber());

        int checkUserBalance = foundUser.getAccountBalance().compareTo(request.getAmount());
        //System.out.println(checkUserBalance);
        if(checkUserBalance < 0)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_BALANCE_NOT_SUFFICIENT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_BALANCE_NOT_SUFFICIENT_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(foundUser.getAccountNumber())
                            .accountName(AccountUtils.fullNameUser(foundUser))
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        }

        foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(request.getAmount()));
        this.userRespository.save(foundUser);

        this.transactionService.saveTransaction(TransactionDto.builder()
                .transactionType("DEBITE")
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .build());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage("Le compte a bien été débité de " + request.getAmount())
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(AccountUtils.fullNameUser(foundUser))
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse TransfertBalance(TransfertRequest request) {

        BankResponse resultDebit = debitAccount(CreditDebitRequest.builder()
                .accountNumber(request.getSourceAccountNumber())
                .amount(request.getAmount())
                .build());

        if(resultDebit.getResponseCode().equals(AccountUtils.ACCOUNT_NO_EXIST_CODE) || resultDebit.getResponseCode().equals(AccountUtils.ACCOUNT_BALANCE_NOT_SUFFICIENT_CODE))
        {
            return resultDebit;
        }

        BankResponse resultCredit = creditAccount(CreditDebitRequest.builder()
                .accountNumber(request.getDestinationAccountNumber())
                .amount(request.getAmount())
                .build());

        if(resultCredit.getResponseCode().equals(AccountUtils.ACCOUNT_NO_EXIST_CODE))
        {
            return resultCredit;
        }

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFERT_SUCCESS_CODE)
                .responseMessage("Votre compte a été débité de la somme de " + request.getAmount() + " pour crédité le compte avec pour numero " + request.getDestinationAccountNumber() + " et pour nom utilisateur " + resultCredit.getAccountInfo().getAccountName())
                .accountInfo(AccountInfo.builder()
                        .accountNumber(resultDebit.getAccountInfo().getAccountNumber())
                        .accountName(resultDebit.getAccountInfo().getAccountName())
                        .accountBalance(resultDebit.getAccountInfo().getAccountBalance())
                        .build())
                .build();
    }
}
