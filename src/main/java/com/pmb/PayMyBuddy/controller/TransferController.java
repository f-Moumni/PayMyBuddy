package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/Transfer")
public class TransferController {
    @Autowired
    TransferService transferService;

    @PostMapping()
    public ResponseEntity<Response> addTransfer(@RequestBody @Valid TransferDTO operation) throws DataNotFoundException, InsufficientFundsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfers", transferService.doTransfer(operation)))
                        .message("transfer sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/Received")
    public ResponseEntity<Response> getReceivedTransfer(@RequestParam @Valid String mail)  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfers", transferService.getReservedTransfers(mail)))
                        .message(" retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @GetMapping("/sent")
    public ResponseEntity<Response> getSentTransfer(@RequestParam @Valid String mail)  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfers", transferService.getSentTransfers(mail)))
                        .message(" retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @GetMapping("/All")
    public ResponseEntity<Response> getAllTransfer(@RequestParam @Valid String mail)  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfers", transferService.getAllTransfers(mail)))
                        .message(" retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
