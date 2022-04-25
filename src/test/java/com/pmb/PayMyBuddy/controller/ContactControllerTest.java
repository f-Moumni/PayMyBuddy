package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.IBankAccountService;
import com.pmb.PayMyBuddy.service.IContactService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import com.pmb.PayMyBuddy.util.TestSecurityConfig;
import org.hibernate.sql.Delete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonTestMapper.class, TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private IContactService contactService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;
    private ContactDTO contactDTO;


    @BeforeEach
    void setup() {
        contactDTO = new ContactDTO("john", "doe","doe@exemple.fr");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    @Tag("GetContacts")
    void testGetContacts_shouldReturnListOfContactDTO() throws Exception {
        //ARRANGE
        when(contactService.getContacts()).thenReturn(Set.of(contactDTO));
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
        //ARRANGE
        when(contactService.deleteContact(anyString())).thenReturn(true);
        //ACT //ASSERT
        mvc.perform(delete("/contact")
                        .contentType(MediaType.APPLICATION_JSON).param("mail", "doe@exemple.fr ")
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
        when(contactService.addContact(anyString())).thenReturn(contactDTO);
        //ACT //ASSERT
        mvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON).param("mail", "doe@exemple.fr ")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Contact added"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['contact'].firstName").value(contactDTO.getFirstName()))
                .andExpect(jsonPath("$.data['contact'].lastName").value(contactDTO.getLastName()))
                .andExpect(jsonPath("$.data['contact'].email").value(contactDTO.getEmail()));
    }
}
