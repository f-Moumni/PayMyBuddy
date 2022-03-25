package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.UserDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.service.UserService;
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
    public ResponseEntity<Response> GetAccount(@RequestParam @Valid String mail) throws DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.getUser(mail)))
                        .message("Account retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping
    public ResponseEntity<Response> updateUserAccount(@RequestBody @Valid UserDTO accountToUpdate) {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.updateUser(accountToUpdate)))
                        .message("Account updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> SaveUserAccount(@RequestBody @Valid UserDTO account) throws AlreadyExistsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.addUser(account)))
                        .message("Account Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteUser(@RequestParam @Valid String mail) throws DataNotFoundException, InsufficientFundsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.deleteUser(mail)))
                        .message("Account deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
