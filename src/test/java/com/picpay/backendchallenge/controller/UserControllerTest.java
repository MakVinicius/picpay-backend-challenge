package com.picpay.backendchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.UserType;
import com.picpay.backendchallenge.service.UserService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ValidUser_Returns201() throws Exception {
        UserDTO validUserDTO = new UserDTO(
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

        doReturn(user).when(userService).createUser(any(UserDTO.class));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(validUserDTO))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userType").value("COMMON"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Maria Souza"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("maria@mail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.document").value("123.456.789-11"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("1200"));

    }

    @Test
    public void createUser_InvalidUser_UserTypeNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            null,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidUserDTO))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void createUser_InvalidUser_FullNameNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            UserType.COMMON,
            null,
            "maria@mail.com",
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void createUser_InvalidUser_EmailNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            UserType.COMMON,
            "Maria Souza",
            null,
            "password",
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void createUser_InvalidUser_PasswordNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            null,
            "123.456.789-11",
            BigDecimal.valueOf(1200)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void createUser_InvalidUser_DocumentNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            null,
            BigDecimal.valueOf(1200)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void createUser_InvalidUser_BalanceNull() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(
            UserType.COMMON,
            "Maria Souza",
            "maria@mail.com",
            "password",
            "123.456.789-11",
            null
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void getAllUsers_Success() throws Exception {
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
        List<User> expectedUsers =new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].document").value("123.456.789-11"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].document").value("123.456.789-12"));
    }
}
