package com.picpay.backendchallenge.infra;

import com.picpay.backendchallenge.dto.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ControllerExceptionHandlerTest {

    @Autowired
    @InjectMocks
    private ControllerExceptionHandler controllerExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void treatDuplicateEntry() {
        DataIntegrityViolationException mockException = mock(DataIntegrityViolationException.class);

        ResponseEntity responseEntity = controllerExceptionHandler.treatDuplicateEntry(mockException);

        assertEquals(400, responseEntity.getStatusCodeValue());
        ExceptionDTO body = new ExceptionDTO("User already exists", HttpStatus.BAD_REQUEST.toString());
        assertEquals("User already exists", body.message());
        assertEquals("400 BAD_REQUEST", body.statusCode());
    }

    @Test
    void treatEntityNotFound() {
        EntityNotFoundException mockException = mock(EntityNotFoundException.class);

        ResponseEntity expectedResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity responseEntity = controllerExceptionHandler.treatEntityNotFound(mockException);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void testTreatDuplicateEntry() {
        Exception mockException = mock(Exception.class);

        ResponseEntity responseEntity = controllerExceptionHandler.treatDuplicateEntry(mockException);

        assertEquals(500, responseEntity.getStatusCodeValue());
        ExceptionDTO body = new ExceptionDTO("Test message", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        assertEquals("Test message", body.message());
        assertEquals("500 INTERNAL_SERVER_ERROR", body.statusCode());
    }
}
