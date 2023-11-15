package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserById(Long id);

    User createUser(UserDTO userDTO);

    User saveUser(User user);

    List<User> getAllUsers();
}
