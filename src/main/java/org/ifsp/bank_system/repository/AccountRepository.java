package org.ifsp.bank_system.repository;

import org.ifsp.bank_system.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {

    private static Map<Integer, Account> accounts = new HashMap<>();

    public void save(Account account) {
        accounts.put(account.getAccountNumber(),  account);
    }

    public boolean delete(Account account) {
        return accounts.remove(account.getAccountNumber(), account);
    }

    public Optional<Account> findByAccountNumber(int accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    public String getAccounts() {
        return accounts.toString();
    }
}
