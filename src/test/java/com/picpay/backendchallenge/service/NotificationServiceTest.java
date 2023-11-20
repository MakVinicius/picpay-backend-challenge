package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.NotificationDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.UserType;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_Successful() {
        User receiver = new User(
            UserType.COMMON,
            "João das Neves",
            "joao@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        String message = "Test Message";

        Map<String, Object> body = new HashMap<>();
        body.put("message", true);
        ResponseEntity<Map> successResponse = new ResponseEntity<>(body, HttpStatus.OK);

        doReturn(successResponse).when(restTemplate).postForEntity(anyString(), any(NotificationDTO.class), eq(Map.class));

        Boolean result = notificationService.sendNotification(receiver, message);

        assertTrue(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(NotificationDTO.class), eq(Map.class));
    }

    @Test
    void sendNotification_NotSuccessful() {
        User receiver = new User(
            UserType.COMMON,
            "João das Neves",
            "joao@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        String message = "Test Message";

        Map<String, Object> body = new HashMap<>();
        body.put("message", false);
        ResponseEntity<Map> successResponse = new ResponseEntity<>(body, HttpStatus.OK);

        doReturn(successResponse).when(restTemplate).postForEntity(anyString(), any(NotificationDTO.class), eq(Map.class));

        Boolean result = notificationService.sendNotification(receiver, message);

        assertFalse(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(NotificationDTO.class), eq(Map.class));
    }
}
