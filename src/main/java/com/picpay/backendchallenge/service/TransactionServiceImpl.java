package com.picpay.backendchallenge.service;

import com.picpay.backendchallenge.dto.TransactionDTO;
import com.picpay.backendchallenge.entity.Transaction;
import com.picpay.backendchallenge.entity.User;
import com.picpay.backendchallenge.enums.Status;
import com.picpay.backendchallenge.enums.UserType;
import com.picpay.backendchallenge.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate;

    @Override
    public Transaction sendMoney(TransactionDTO transactionDTO) throws Exception {
        User sender = userService.findUserById(transactionDTO.sender())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        User receiver = userService.findUserById(transactionDTO.receiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        if (sender.getUserType().equals(UserType.STORE)) {
            throw new Exception("Stores can't send money to users");
        }

        BigDecimal possibleTransaction = sender.getBalance().subtract(transactionDTO.amount());

        if (possibleTransaction.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Balance not enough");
        }

        if (!authorizeTransaction()) {
            Transaction failedTransaction = new Transaction(LocalDateTime.now(), Status.Failure, sender, receiver, transactionDTO.amount(), false);
            transactionRepository.save(failedTransaction);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction not authorized");
        }

        sender.setBalance(possibleTransaction);
        receiver.setBalance(receiver.getBalance().add(transactionDTO.amount()));

        Transaction newTransaction = new Transaction(LocalDateTime.now(), Status.Successful, sender, receiver, transactionDTO.amount(), false);

        userService.saveUser(sender);
        userService.saveUser(receiver);

        Boolean notification = notificationService.sendNotification(receiver, "Transaction of R$ " + transactionDTO.amount() + " received in your account");
        if (notification) {
            newTransaction.setNotification(true);
        }

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return savedTransaction;
    }

    @Override
    public Boolean authorizeTransaction() {
        ResponseEntity<Map> authorized = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

        if (authorized.getStatusCode().equals(HttpStatus.OK)) {
            String message = authorized.getBody().get("message").toString();

            return "Autorizado".equalsIgnoreCase(message);
        }

        return false;
    }
}
