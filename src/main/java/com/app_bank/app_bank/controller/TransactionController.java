package com.app_bank.app_bank.controller;

import com.app_bank.app_bank.dto.StatementRequest;
import com.app_bank.app_bank.services.BankStatement;
import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private BankStatement bankStatement;

    public TransactionController(BankStatement bankStatement) {

        this.bankStatement = bankStatement;
    }

    @GetMapping("/statements")
    public List generateStatement(@RequestBody  StatementRequest request) throws DocumentException, FileNotFoundException {

        return this.bankStatement.generateStatement(request);
    }
}
