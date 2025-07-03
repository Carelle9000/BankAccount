package com.example.bank_management;

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
        BankAccount account = service.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        model.addAttribute("account", account);
        return "edit_account";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable Long id, @ModelAttribute BankAccount account) {
        service.updateAccount(id, account);
        return "redirect:/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
        return "redirect:/accounts";
    }

    @GetMapping("/deposit/{id}")
    public String showDepositForm(@PathVariable Long id, Model model) {
        model.addAttribute("account", service.getAccountById(id).orElseThrow());
        model.addAttribute("amount", 0.0);
        return "deposit";
    }

    @PostMapping("/deposit/{id}")
    public String deposit(@PathVariable Long id, @RequestParam double amount) {
        service.deposit(id, amount);
        return "redirect:/accounts";
    }

    @GetMapping("/withdraw/{id}")
    public String showWithdrawForm(@PathVariable Long id, Model model) {
        model.addAttribute("account", service.getAccountById(id).orElseThrow());
        model.addAttribute("amount", 0.0);
        return "withdraw";
    }

    @PostMapping("/withdraw/{id}")
    public String withdraw(@PathVariable Long id, @RequestParam double amount) {
        service.withdraw(id, amount);
        return "redirect:/accounts";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("accounts", service.getAllAccounts());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        service.transfer(fromId, toId, amount);
        return "redirect:/accounts";
    }
}