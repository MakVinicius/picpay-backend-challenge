package com.picpay.backendchallenge.controller;

import com.picpay.backendchallenge.dto.TransactionDTO;
import com.picpay.backendchallenge.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> sendMoney(@Valid @RequestBody TransactionDTO transactionDTO) throws Exception {
        return new ResponseEntity<>(
                transactionService.sendMoney(transactionDTO),
                HttpStatus.OK
        );
    }
}
