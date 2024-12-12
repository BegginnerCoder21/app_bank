package com.app_bank.app_bank.controller;

import com.app_bank.app_bank.dto.BankResponse;
import com.app_bank.app_bank.dto.UserRequest;
import com.app_bank.app_bank.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
