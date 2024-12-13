package com.app_bank.app_bank.controller;

import com.app_bank.app_bank.dto.*;
import com.app_bank.app_bank.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public BankResponse createAccount(@RequestBody UserRequest userRequest)
    {

       return this.userService.createAccount(userRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return this.userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return this.userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request)
    {
        return this.userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request)
    {
        return this.userService.debitAccount(request);
    }

    @PostMapping("/transfertBalance")
    public BankResponse transfertAccount(@RequestBody TransfertRequest request)
    {
        return this.userService.TransfertBalance(request);
    }
}
