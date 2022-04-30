package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.Response;

import com.pmb.PayMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

import static java.time.LocalDateTime.now;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transactions")
public class TransactionController {
@Autowired
    TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Response> getTransactions()  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transactions", transactionService.getAllTransactions()))
                        .message("all transaction got with success ")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
