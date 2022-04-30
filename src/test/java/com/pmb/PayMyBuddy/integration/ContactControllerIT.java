package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.ContactDTO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ContactControllerIT {

    @Autowired
    private MockMvc mvc;

    private ContactDTO contactDTO;
    private static User user;
    private static SecurityContext securitycontext;

    @BeforeAll
    public static void init() {
        securitycontext = new SecurityContextImpl();
        user = new User("doe@exemple.fr", "password", List.of(new SimpleGrantedAuthority("USER")));
    }

    @BeforeEach
    void setup() throws SQLException {
        contactDTO = new ContactDTO("john", "doe", "john@exemple.fr");
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
    }


    @Test
    @Tag("GetContacts")
    void testGetContacts_shouldReturnListOfContactDTO() throws Exception {
        //ACT //ASSERT
        mvc.perform(get("/contact/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Contacts retrieved"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['contacts'].[0].firstName").value(contactDTO.getFirstName()))
                .andExpect(jsonPath("$.data['contacts'].[0].lastName").value(contactDTO.getLastName()))
                .andExpect(jsonPath("$.data['contacts'].[0].email").value(contactDTO.getEmail()));
    }

    @Test
    @Tag("RemoveContact")
    void testRemoveContact_shouldReturnTrue() throws Exception {

        //ACT //ASSERT
        mvc.perform(delete("/contact")
                        .contentType(MediaType.APPLICATION_JSON).param("mail", "john@exemple.fr")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Contact removed"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['contact']").value(true));
    }

    @Test
    @Tag("AddContact")
    void testAddContact_shouldReturnContactDTO() throws Exception {
//ARRANGE
        contactDTO = new ContactDTO("pmb", "account", "pmb@exemple.fr");
        //ACT //ASSERT
        mvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON).param("mail", "pmb@exemple.fr")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Contact added"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['contact'].firstName").value(contactDTO.getFirstName()))
                .andExpect(jsonPath("$.data['contact'].lastName").value(contactDTO.getLastName()))
                .andExpect(jsonPath("$.data['contact'].email").value(contactDTO.getEmail()));
    }
}
