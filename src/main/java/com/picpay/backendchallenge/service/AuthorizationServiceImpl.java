package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final RestTemplate restTemplate;

    @Override
    public Boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorized = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

        if (authorized.getStatusCode().equals(HttpStatus.OK)) {
            String message = authorized.getBody().get("message").toString();

            return "Autorizado".equalsIgnoreCase(message);
        }

        return false;
    }
}
