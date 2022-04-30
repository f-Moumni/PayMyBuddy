package com.pmb.PayMyBuddy.security;


import com.pmb.PayMyBuddy.security.jwt.AuthTokenFilter;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.util.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {
    private static final String token = "260bce87-6be9-4897-add7-b3b675952538";
    private static final String testUri = "/testUri";

    @Mock
    private static AccountDetailsService accountDetailsService;
    private static MockHttpServletRequest request;
    private static MockHttpServletResponse response;
    @Mock
    private static FilterChain filterChain;
    @Mock
    private static JwtUtils jwtUtils;
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal Positive Scenario When Token Is In Header")
    public void testDoFilterInternal_PositiveScenario() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("doe@exemple.fr");
        doReturn(new User("doe@exemple.fr", "password", new ArrayList<>())).when(accountDetailsService).loadUserByUsername("doe@exemple.fr");

        //ACT
        authTokenFilter.doFilterInternal(request, response, filterChain);
        //ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal When Token Is not In Header")
    public void testDoFilterInternal_WhenTokenIsNotInHeader() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", " ");
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();

        //ACT
        authTokenFilter.doFilterInternal(request, response, filterChain);
        //ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal With out Authorization In Header")
    public void testDoFilterInternal_WhenWithOutAuthorization() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader(" ", "Bearer " + token);
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();

        //ACT
        authTokenFilter.doFilterInternal(request, response, filterChain);
        //ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal When Token Is not valid")
    public void testDoFilterInternal_WhenTokenIsNotValid() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        //ACT
        authTokenFilter.doFilterInternal(request, response, filterChain);
        //ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal accountDetailsService throw exception ")
    public void testDoFilterInternal_withException() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("doe@exemple.fr");
        doThrow(UsernameNotFoundException.class).when(accountDetailsService).loadUserByUsername("doe@exemple.fr");
        //ACT//ARRANGE
        assertThrows(UsernameNotFoundException.class, () -> authTokenFilter.doFilterInternal(request, response, filterChain));
    }
    @Test
    @Tag("DoFilterInternal")
    @DisplayName("test DoFilterInternal When Header is empty")
    public void testDoFilterInternal_withEmptyHeader() throws ServletException, IOException {
        //ARRANGE
        request = new MockHttpServletRequest();
        request.addHeader("", " " );
        request.setRequestURI(testUri);
        response = new MockHttpServletResponse();
        //ACT
        authTokenFilter.doFilterInternal(request, response, filterChain);
        //ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(filterChain).doFilter(request, response);
    }
}
