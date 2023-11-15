package com.picpay.backendchallenge.entity;

import com.picpay.backendchallenge.dto.UserDTO;
import com.picpay.backendchallenge.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserType userType;

    @Column(length = 100)
    private String fullName;

    @Email
    @Column(length = 30, unique = true)
    private String email;

    private String password;

    @Column(length = 19, unique = true)
    private String document;

    private BigDecimal balance;

    public User(UserType userType, String fullName, String email, String password, String document, BigDecimal balance) {
        this.userType = userType;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.document = document;
        this.balance = balance;
    }

    public User(UserDTO userDTO) {
        this.userType = userDTO.userType();
        this.fullName = userDTO.fullName();
        this.email = userDTO.email();
        this.password = userDTO.password();
        this.document = userDTO.document();
        this.balance = userDTO.balance();
    }
}
