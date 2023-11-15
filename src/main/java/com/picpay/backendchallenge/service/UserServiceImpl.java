package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User newUser = new User(userDTO);
        return userRepository.save(newUser);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
