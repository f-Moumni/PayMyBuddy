package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.service.IPaymentService;
import com.pmb.PayMyBuddy.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * PaymentController  class allows to add and get all Payments
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    IPaymentService paymentService;

    /**
     * the end point to get all payments of current user
     * @return response with list of transactionDTO
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllPayments()  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transactions", paymentService.getAllPayments()))
                        .message("all retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /**
     * the end point to do new payment
     * @param operation payment DTO to do
     * @return response with True if payment is done
     * @throws InsufficientFundsException
     */
    @PostMapping()
    public ResponseEntity<Response> doPayment(@RequestBody @Valid PaymentDTO operation) throws InsufficientFundsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("payment", paymentService.doPayment(operation)))
                        .message("payment done")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }
}
