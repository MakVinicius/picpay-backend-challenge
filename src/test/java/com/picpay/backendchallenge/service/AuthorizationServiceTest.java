package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class AuthorizationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authorizeTransaction_WhenAuthorized() {
        Map<String, Object> responseBody = Collections.singletonMap("message", "Autorizado");
        ResponseEntity<Map<String, Object>> authorizedResponse = ResponseEntity.ok(responseBody);
        doReturn(authorizedResponse).when(restTemplate).getForEntity(any(String.class), any());

        boolean isAuthorized = authorizationService.authorizeTransaction(new User(), BigDecimal.valueOf(100));

        assertTrue(isAuthorized);
    }

    @Test
    public void testAuthorizeTransaction_WhenHTTPNotAuthorized() {
        ResponseEntity<Map<String, Object>> unauthorizedResponse = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        doReturn(unauthorizedResponse).when(restTemplate).getForEntity(any(String.class), any());

        boolean isAuthorized = authorizationService.authorizeTransaction(new User(), BigDecimal.valueOf(100));

        assertFalse(isAuthorized);
    }

    @Test
    void authorizeTransaction_WhenMessageNotAuthorized() {
        Map<String, Object> responseBody = Collections.singletonMap("message", "Negado");
        ResponseEntity<Map<String, Object>> unauthorizedResponse = ResponseEntity.ok(responseBody);
        doReturn(unauthorizedResponse).when(restTemplate).getForEntity(any(String.class), any());

        boolean isAuthorized = authorizationService.authorizeTransaction(new User(), BigDecimal.valueOf(100));

        assertFalse(isAuthorized);
    }
}
