package com.picpay.backendchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.backendchallenge.dto.TransactionDTO;
import com.picpay.backendchallenge.entity.Transaction;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.Status;
import com.picpay.backendchallenge.enums.UserType;
import com.picpay.backendchallenge.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TransactionsControllerTest {
    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMoney_Success() throws Exception {
        User sender = new User(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );
        sender.setId(1L);

        User receiver = new User(
            UserType.COMMON,
            "Carlos Moura",
            "carlos@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        TransactionDTO transactionDTO = new TransactionDTO(1L, 2L, new BigDecimal(100));
        UUID uuid = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(timestamp, Status.Successful, sender, receiver, new BigDecimal(100), true);
        transaction.setId(uuid);

        when(transactionService.sendMoney(any(TransactionDTO.class))).thenReturn(transaction);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transactionDTO))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(uuid.toString()))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.Successful.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sender.id").value(1L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.receiver.id").value(2L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100))
            .andExpect(MockMvcResultMatchers.jsonPath("$.notification").value(true));

    }

    @Test
    void sendMoney_Fail() throws Exception {
        User sender = new User(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );
        sender.setId(1L);

        User receiver = new User(
            UserType.COMMON,
            "Carlos Moura",
            "carlos@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        TransactionDTO transactionDTO = new TransactionDTO(1L, 2L, new BigDecimal(100));
        UUID uuid = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(timestamp, Status.Successful, sender, receiver, new BigDecimal(100), true);
        transaction.setId(uuid);

        when(transactionService.sendMoney(any(TransactionDTO.class))).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction not authorized"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transactionDTO))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("500 INTERNAL_SERVER_ERROR \"Transaction not authorized\""));
    }
}
