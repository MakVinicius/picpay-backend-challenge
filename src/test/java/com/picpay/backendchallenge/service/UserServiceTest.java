package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.UserType;
import com.picpay.backendchallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserById_Success() {
        User user = new User(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById(1L);

        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getUserType(), foundUser.get().getUserType());
        assertEquals(user.getFullName(), foundUser.get().getFullName());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
        assertEquals(user.getDocument(), foundUser.get().getDocument());
        assertEquals(user.getBalance(), foundUser.get().getBalance());
    }

    @Test
    void createUser_Success() {
        UserDTO userDTO = new UserDTO(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        User user = new User(
                UserType.COMMON,
                "Maria Souza",
                "maria@mail.com",
                "password",
                "123.456.789-11",
                BigDecimal.valueOf(1200)
        );
        user.setId(1L);

        when(userRepository.save(any())).thenReturn(user);

        User newUser = userService.createUser(userDTO);

        assertEquals(userDTO.userType(), newUser.getUserType());
        assertEquals(userDTO.fullName(), newUser.getFullName());
        assertEquals(userDTO.email(), newUser.getEmail());
        assertEquals(userDTO.password(), newUser.getPassword());
        assertEquals(userDTO.document(), newUser.getDocument());
        assertEquals(userDTO.balance(), newUser.getBalance());
    }

    @Test
    void saveUser_Success() {
        String fullName = "Maria Souza";
        String email = "maria@mail.com";
        String password = "password";
        String document = "123.456.789-11";
        BigDecimal balance = BigDecimal.valueOf(1200);

        User user = new User(
            UserType.COMMON,
            fullName,
            email,
            password,
            document,
            balance
        );

        when(userRepository.save(any(User.class))).thenReturn(user);

        User newUser = userService.saveUser(user);

        assertEquals(UserType.COMMON, newUser.getUserType());
        assertEquals(fullName, newUser.getFullName());
        assertEquals(email, newUser.getEmail());
        assertEquals(password, newUser.getPassword());
        assertEquals(document, newUser.getDocument());
        assertEquals(balance, newUser.getBalance());
    }

    @Test
    void getAllUsers_Success() {
        User user1 = new User(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        User user2 = new User(
            UserType.COMMON,
            "Carlos Moura",
            "carlos@mail.com",
            "password",
            "123.456.789-12",
            BigDecimal.valueOf(1000)
        );

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> foundUsers = userService.getAllUsers();

        assertEquals(foundUsers.get(0), user1);
        assertEquals(foundUsers.get(1), user2);
    }
}
