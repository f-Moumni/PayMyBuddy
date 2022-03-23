package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/BankAccount")
public class BankAccountController {
    @Autowired
    IBankAccountService bankAccountService;

    @GetMapping
    public ResponseEntity<Response> getBankAccount(@RequestParam @Valid String mail) throws DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("bankAccount", bankAccountService.getBankAccount(mail)))
                        .message("Bank Account retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> saveBankAccount(@RequestBody BankAccountDTO bAccountToAdd, @RequestParam @Valid String mail) {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("bankAccount", bankAccountService.addBankAccount(bAccountToAdd,mail)))
                        .message("Bank Account saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }
}
