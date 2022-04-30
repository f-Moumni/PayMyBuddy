package com.pmb.PayMyBuddy.controller;


import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

/**
 * ContactController class allows to do CRUD operations for Contact
 */
@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
public class ContactController {
@Autowired
IContactService contactService;

    /**
     * endpoint to Add new contact in contacts list of current user
     * @param mail the contact email
     * @return  contact DTO of contact added
     * @throws AlreadyExistsException
     * @throws DataNotFoundException
     */
    @PostMapping
    public ResponseEntity<Response> addContact(@RequestParam @Valid String mail ) throws AlreadyExistsException, DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contact", contactService.addContact(mail)))
                        .message("Contact added")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /**
     * endpoint to remove a given contact from  contacts list of current user
     * @param mail contact email
     * @return response with true if removed
     */
    @DeleteMapping
    public ResponseEntity<Response> removeContact(@RequestParam @Valid String mail) {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contact", contactService.deleteContact(mail)))
                        .message("Contact removed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /**
     * end point to get all contacts of current user
     * @return list of ContactDTO
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getContacts(){
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contacts", contactService.getContacts()))
                        .message("Contacts retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
