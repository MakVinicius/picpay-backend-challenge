package com.picpay.backendchallenge.entity;

import com.picpay.backendchallenge.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "history-transactions")
@Entity
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private BigDecimal amount;

    private Boolean notification;

    public Transaction(LocalDateTime timestamp, Status status, User sender, User receiver, BigDecimal amount, Boolean notification) {
        this.timestamp = timestamp;
        this.status = status;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.notification = notification;
    }
}
