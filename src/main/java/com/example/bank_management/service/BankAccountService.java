package com.example.bank_management.service;

import com.example.bank_management.model.BankAccount;
import com.example.bank_management.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    public List<BankAccount> getAllAccounts() {
        return repository.findAll();
    }

    public Optional<BankAccount> getAccountById(Long id) {
        return repository.findById(id);
    }

    public BankAccount createAccount(BankAccount account) {
        return repository.save(account);
    }

    public BankAccount updateAccount(Long id, BankAccount accountDetails) {
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setFirstName(accountDetails.getFirstName());
        account.setLastName(accountDetails.getLastName());
        account.setEmail(accountDetails.getEmail());
        return repository.save(account);
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public BankAccount deposit(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        return repository.save(account);
    }

    @Transactional
    public BankAccount withdraw(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        return repository.save(account);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        BankAccount fromAccount = repository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        BankAccount toAccount = repository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in source account");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        repository.save(fromAccount);
        repository.save(toAccount);
    }
}