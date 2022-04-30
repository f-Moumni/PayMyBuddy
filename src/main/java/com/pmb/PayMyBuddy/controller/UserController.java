package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.service.IUserService;
import com.pmb.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * UserController class allows to do CRUD operations for User
 */
@RestController
@RequestMapping(value = "api")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * endpoint to get profile of current user
     * @returnr response with profile of current user
     * @throws DataNotFoundException
     */
    @GetMapping("account")
    public ResponseEntity<Response> GetProfile() throws DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.getUser()))
                        .message("Account retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /**
     * end point to update current user account
     * @param accountToUpdate
     * @return
     */
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

    /**
     * end point to register a new user
     * @param account SignupDTO of a new user
     * @return response with true is saved
     * @throws AlreadyExistsException
     */
    @PostMapping("sign-up")
    public ResponseEntity<Response> saveUserAccount(@RequestBody @Valid SignupDTO account) throws AlreadyExistsException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.addUser(account)))
                        .message("Account Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    /**
     * end point to delete user from application
     * @return response withe true is done
     * @throws DataNotFoundException
     * @throws BalanceNotEmptyException
     */
    @DeleteMapping("account")
    public ResponseEntity<Response> deleteUser() throws DataNotFoundException, BalanceNotEmptyException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("account", userService.deleteUser()))
                        .message("Account deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
