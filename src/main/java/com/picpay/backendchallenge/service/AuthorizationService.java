package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.entity.User;

import java.math.BigDecimal;

public interface AuthorizationService {
    Boolean authorizeTransaction(User sender, BigDecimal value);
}
