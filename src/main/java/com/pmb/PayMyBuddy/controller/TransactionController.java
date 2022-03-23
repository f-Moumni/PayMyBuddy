package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.DTO.PaymentDTO;
import com.pmb.paymybuddy.DTO.Response;
import com.pmb.paymybuddy.DTO.TransferDTO;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.service.IContactService;
import com.pmb.paymybuddy.service.IPaymentService;
import com.pmb.paymybuddy.service.ITransactionService;
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
    ITransactionService transactionService;

    @PostMapping("payment")
    public ResponseEntity<Response> addPayment(@RequestBody @Valid PaymentDTO operation) throws BalanceException, DataNoteFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("payment", transactionService.addPayment(operation)))
                        .message("payment sent ")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @PostMapping("transfer")
    public ResponseEntity<Response> addTransfer(@RequestBody @Valid TransferDTO operation) throws BalanceException, DataNoteFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfer", transactionService.addTransfer(operation)))
                        .message("transfer sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @GetMapping
    public ResponseEntity<Response> getTransactions(@RequestParam @Valid String mail) throws BalanceException, DataNoteFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("payment", transactionService.getAllTransactions(mail)))
                        .message("all transaction got with success ")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
