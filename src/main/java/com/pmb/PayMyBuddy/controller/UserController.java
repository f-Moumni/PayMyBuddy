package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
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
@RequestMapping(value = "api")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("account")
    public ResponseEntity<Response> GetProfile() throws DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.getUser(PrincipalUser.getCurrentUserMail())))
                        .message("Account retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping("account")
    public ResponseEntity<Response> updateUserAccount(@RequestBody @Valid SignupDTO accountToUpdate) {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.updateUser(accountToUpdate)))
                        .message("Account updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("sign-up")
    public ResponseEntity<Response> SaveUserAccount(@RequestBody @Valid SignupDTO account) throws AlreadyExistsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.addUser(account)))
                        .message("Account Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @DeleteMapping("account")
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
