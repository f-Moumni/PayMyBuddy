package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;


    @GetMapping("/all")
    public ResponseEntity<Response> getAllPayment()  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transactions", paymentService.getAllPayments()))
                        .message(" all retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @PostMapping()
    public ResponseEntity<Response> addPayment(@RequestBody @Valid PaymentDTO operation) throws InsufficientFundsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("payment", paymentService.doPayment(operation)))
                        .message("payment sent ")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }
}
