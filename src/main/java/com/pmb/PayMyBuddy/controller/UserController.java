package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.DTO.Response;
import com.pmb.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "account")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<Response> GetAccount(@RequestParam @Valid String mail) throws DataNoteFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.getUser(mail)))
                        .message("Account retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

     @PutMapping
    public ResponseEntity<Response> updateUserAccount(@RequestBody @Valid AccountDTO accountToUpdate) throws DataNoteFoundException {
         return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.updateUser(accountToUpdate)))
                        .message("Account updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @PostMapping
    public ResponseEntity<Response> SaveUserAccount(@RequestBody @Valid AccountDTO account) throws AlreadyExistsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.addUser(account)))
                        .message("Account Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }
    @DeleteMapping
    public ResponseEntity<Response> deleteUser(@RequestBody @Valid AccountDTO account) throws BalanceException, DataNoteFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.deleteUser(account)))
                        .message("Account updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
