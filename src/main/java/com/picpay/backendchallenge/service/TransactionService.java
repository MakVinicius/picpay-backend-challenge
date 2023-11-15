package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.TransactionDTO;
import com.picpay.backendchallenge.entity.Transaction;

public interface TransactionService {

    Transaction sendMoney(TransactionDTO transactionToUserDTO) throws Exception;
}
