package org.ifsp.bank_system.service;

import org.ifsp.bank_system.model.Account;
import org.ifsp.bank_system.model.Client;
import org.ifsp.bank_system.model.CurrentAccount;
import org.ifsp.bank_system.model.SavingsAccount;
import org.ifsp.bank_system.repository.AccountRepository;

import java.math.BigDecimal;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createCurrentAccount() {
        Client loggedInClient = SessionManager.getLoggedInClient()
                .orElseThrow(() -> new IllegalStateException("Nenhum usuário logado!"));

        Account newAccount = new CurrentAccount(loggedInClient);

        accountRepository.save(newAccount);

        return newAccount;
    }

    public Account createSavingsAccount() {

        Client loggedInClient = SessionManager.getLoggedInClient()
                .orElseThrow(() -> new IllegalStateException("Nenhum usuário logado!"));

        Account newAccount = new SavingsAccount(loggedInClient);

        accountRepository.save(newAccount);

        return newAccount;
    }

    public void transferMoney(Account fromAccount, Account toAccount, BigDecimal amount) {

    }
}
