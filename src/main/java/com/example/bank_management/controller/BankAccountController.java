package com.example.bank_management.controller;

import com.example.bank_management.model.BankAccount;
import com.example.bank_management.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    @GetMapping
    public String listAccounts(Model model) {
        model.addAttribute("accounts", service.getAllAccounts());
        return "accounts";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("account", new BankAccount());
        return "create_account";
    }

    @PostMapping
    public String createAccount(@ModelAttribute BankAccount account) {
        service.createAccount(account);
        return "redirect:/accounts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            BankAccount account = service.getAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
            model.addAttribute("account", account);
            return "edit_account";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable Long id, @ModelAttribute BankAccount account) {
        try {
            service.updateAccount(id, account);
            return "redirect:/accounts";
        } catch (IllegalArgumentException ex) {
            return "redirect:/error?message=" + ex.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        try {
            service.deleteAccount(id);
            return "redirect:/accounts";
        } catch (IllegalArgumentException ex) {
            return "redirect:/error?message=" + ex.getMessage();
        }
    }

    @GetMapping("/deposit/{id}")
    public String showDepositForm(@PathVariable Long id, Model model) {
        try {
            BankAccount account = service.getAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
            model.addAttribute("account", account);
            return "deposit";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/deposit/{id}")
    public String deposit(@PathVariable Long id, @RequestParam double amount, Model model) {
        try {
            service.deposit(id, amount);
            return "redirect:/accounts";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("account", service.getAccountById(id).orElse(null));
            model.addAttribute("errorMessage", ex.getMessage());
            return "deposit";
        }
    }

    @GetMapping("/withdraw/{id}")
    public String showWithdrawForm(@PathVariable Long id, Model model) {
        try {
            BankAccount account = service.getAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
            model.addAttribute("account", account);
            return "withdraw";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/withdraw/{id}")
    public String withdraw(@PathVariable Long id, @RequestParam double amount, Model model) {
        try {
            service.withdraw(id, amount);
            return "redirect:/accounts";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("account", service.getAccountById(id).orElse(null));
            model.addAttribute("errorMessage", ex.getMessage());
            return "withdraw";
        }
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("accounts", service.getAllAccounts());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount, Model model) {
        try {
            if (fromId.equals(toId)) {
                throw new IllegalArgumentException("Source and destination accounts must be different");
            }
            service.transfer(fromId, toId, amount);
            return "redirect:/accounts";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("accounts", service.getAllAccounts());
            model.addAttribute("errorMessage", ex.getMessage());
            return "transfer";
        }
    }
}