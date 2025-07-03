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

    public void createAccount(BankAccount account) {
        repository.save(account);
    }

    public void updateAccount(Long id, BankAccount updatedAccount) {
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setFirstName(updatedAccount.getFirstName());
        account.setLastName(updatedAccount.getLastName());
        account.setEmail(updatedAccount.getEmail());
        repository.save(account);
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deposit(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        repository.save(account);
    }

    @Transactional
    public void withdraw(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        repository.save(account);
    }

    @Transactional
    public void transfer(Long id, Long toId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (id.equals(toId)) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }
        BankAccount fromAccount = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        BankAccount toAccount = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in source account");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        repository.save(fromAccount);
        repository.save(toAccount);
    }
}