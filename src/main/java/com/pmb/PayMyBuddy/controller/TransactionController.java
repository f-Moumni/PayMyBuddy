package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.service.ITransactionService;
import com.pmb.PayMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
@Autowired
    TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Response> getTransactions(@RequestParam @Valid String mail)  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("Transactions", transactionService.getAllTransactions(mail)))
                        .message("all transaction got with success ")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
