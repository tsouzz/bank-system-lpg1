package org.ifsp.bank_system;

import org.ifsp.bank_system.controller.MenuController;
import org.ifsp.bank_system.repository.AccountRepository;
import org.ifsp.bank_system.repository.ClientRepository;
import org.ifsp.bank_system.service.AccountService;
import org.ifsp.bank_system.service.ClientService;
import org.ifsp.bank_system.service.LoanService;

public class Main {

    public static void main(String[] args) {

        ClientRepository  clientRepository  = new ClientRepository();
        AccountRepository accountRepository = new AccountRepository();

        ClientService  clientService  = new ClientService(clientRepository);
        AccountService accountService = new AccountService(accountRepository);
        LoanService    loanService    = new LoanService();

        MenuController menu = new MenuController(
                accountService,
                clientService,
                loanService,
                accountRepository,
                clientRepository
        );

        menu.start();
    }
}
