package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.service.ITransferService;
import com.pmb.PayMyBuddy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

/**
 * TransferController allow to gdo and get all transfers
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    ITransferService transferService;

    /**
     * end point to do new transfer
     * @param operation transferDTO of transfer to do
     * @return response with true is transfer done
     * @throws DataNotFoundException
     * @throws InsufficientFundsException
     */
    @PostMapping()
    public ResponseEntity<Response> doTransfer(@RequestBody @Valid TransferDTO operation) throws DataNotFoundException, InsufficientFundsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transfer", transferService.doTransfer(operation)))
                        .message("transfer done")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /**
     * get all transfers of current user
     * @return response with list of transactionDTO
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransfer()  {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("transactions", transferService.getAllTransfers()))
                        .message("all retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
