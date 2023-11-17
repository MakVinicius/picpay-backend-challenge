package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.TransactionDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.UserType;
import com.picpay.backendchallenge.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuthorizationService authorizationService;

    @Autowired
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is okay")
    void sendMoneySuccess() throws Exception {
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
            "João das Neves",
            "joao@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        when(userService.findUserById(1L)).thenReturn(Optional.of(sender));
        when(userService.findUserById(2L)).thenReturn(Optional.of(receiver));

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
        transactionService.sendMoney(request);

        verify(transactionRepository, times(1)).save(any());
        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(receiver, "Transaction of R$ " + request.amount() + " received in your account");
    }

    @Test
    @DisplayName("Should throw exception when transaction is not allowed")
    void sendMoneyFailure() {
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
            "João das Neves",
            "joao@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );
        receiver.setId(2L);

        when(userService.findUserById(1L)).thenReturn(Optional.of(sender));
        when(userService.findUserById(2L)).thenReturn(Optional.of(receiver));

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

        ResponseStatusException exceptionThrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
            transactionService.sendMoney(request);
        });

        Assertions.assertEquals("Transaction not authorized", exceptionThrown.getReason());
    }
}
