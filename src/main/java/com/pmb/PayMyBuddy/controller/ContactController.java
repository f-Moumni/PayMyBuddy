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
@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
public class ContactController {
@Autowired
IContactService contactService;

/*    @GetMapping
    public ResponseEntity<Response> getContact(@RequestParam @Valid String mail) throws DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contact", contactService.getContact(mail)))
                        .message("Contact retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }*/
    @PostMapping
    public ResponseEntity<Response> addContact(@RequestParam @Valid String mail ) throws AlreadyExistsException, DataNotFoundException {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contact", contactService.addContact(mail, PrincipalUser.getCurrentUserMail())))
                        .message("Contact added")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @DeleteMapping
    public ResponseEntity<Response> removeContact(@RequestParam @Valid String mail) {
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contact", contactService.deleteContact(mail,PrincipalUser.getCurrentUserMail())))
                        .message("Contact removed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    @GetMapping("/all")
    public ResponseEntity<Response> getContacts(){
        return ResponseEntity.ok(
                Response.builder().timeStamp(now())
                        .data(Map.of("contacts", contactService.getContacts(PrincipalUser.getCurrentUserMail())))
                        .message("Contacts retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
