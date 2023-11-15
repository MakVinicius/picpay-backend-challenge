package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.entity.User;

public interface NotificationService {
    Boolean sendNotification(User user, String message);
}
