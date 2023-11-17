package com.picpay.backendchallenge.repository;

import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.UserType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get user successfully from DB")
    void findUserByDocumentSuccess() {
        String document = "123.456.789-11";
        UserDTO data = new UserDTO(
            UserType.COMMON,
            "Marcela Brenda",
            "marcela@mail.com",
            "password",
            document,
            new BigDecimal(100)
        );

        this.createUser(data);
        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from DB when user doesn't exists")
    void findUserByDocumentFailure() {
        String document = "123.456.789-11";

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDTO userDTO) {
        User newUser = new User(userDTO);

        this.entityManager.persist(newUser);
        return newUser;
    }
}
