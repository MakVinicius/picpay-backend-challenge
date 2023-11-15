package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.NotificationDTO;
import com.picpay.backendchallenge.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RestTemplate restTemplate;

    @Override
    public Boolean sendNotification(User user, String message) {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<Map> notificationResponse = restTemplate.postForEntity("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6", notificationRequest, Map.class);

        if (!notificationResponse.getBody().get("message").toString().equalsIgnoreCase("true")) {
            System.out.println("Error in sending notification");
            return false;
        }

        return true;
    }
}
