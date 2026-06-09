package org.ifsp.bank_system.controller;

import org.ifsp.bank_system.exception.InsufficientFundsException;
import org.ifsp.bank_system.exception.InvalidOperationException;
import org.ifsp.bank_system.model.Account;
import org.ifsp.bank_system.model.Address;
import org.ifsp.bank_system.model.Client;
import org.ifsp.bank_system.model.NaturalPersonClient;
import org.ifsp.bank_system.repository.AccountRepository;
import org.ifsp.bank_system.repository.ClientRepository;
import org.ifsp.bank_system.service.AccountService;
import org.ifsp.bank_system.service.ClientService;
import org.ifsp.bank_system.service.LoanService;
import org.ifsp.bank_system.service.SessionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuController {

    private final AccountService accountService;
    private final ClientService clientService;
    private final LoanService loanService;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final Scanner scanner;

    private final ArrayList<String> sessionLog = new ArrayList<>();

    public MenuController(AccountService accountService,
                          ClientService clientService,
                          LoanService loanService,
                          AccountRepository accountRepository,
                          ClientRepository clientRepository) {

        this.accountService = accountService;
        this.clientService = clientService;
        this.loanService = loanService;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            if (SessionManager.isUserLoggedIn()) {
                running = showAuthenticatedMenu();
            } else {
                running = showGuestMenu();
            }
        }
        System.out.println("Fechando sistema...");
        scanner.close();
    }

    private boolean showGuestMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Cadastrar cliente");
        System.out.println("0. Sair");

        int option = readInt("Opcao: ");

        switch (option) {
            case 1 -> handleLogin();
            case 2 -> handleRegister();
            case 0 -> { return false; }
            default -> System.out.println("Opcao invalida.");
        }
        return true;
    }

    private boolean showAuthenticatedMenu() {
        Client client = SessionManager.getLoggedInClient().orElseThrow();
        String name = client instanceof NaturalPersonClient npClient ? npClient.getName() : "Cliente";

        System.out.println("\n--- Menu (" + name + " | " + (client.isVip() ? "VIP" : "Padrao") + ") ---");
        System.out.println("1. Abrir conta corrente");
        System.out.println("2. Abrir conta poupanca");
        System.out.println("3. Depositar");
        System.out.println("4. Sacar");
        System.out.println("5. Transferir");
        System.out.println("6. Consultar saldo");
        System.out.println("7. Simular emprestimo SAC");
        System.out.println("8. Consultar total de contas armazenadas");
        System.out.println("9. Historico da sessao");
        System.out.println("10. Logout");
        System.out.println("0. Sair");

        int option = readInt("Opcao: ");

        switch (option) {
            case 1 -> handleOpenCurrentAccount();
            case 2 -> handleOpenSavingsAccount();
            case 3 -> handleDeposit();
            case 4 -> handleWithdraw();
            case 5 -> handleTransfer();
            case 6 -> handleBalance();
            case 7 -> handleLoanSimulation();
            case 8 -> handleTotalOfAccounts();
            case 9 -> handleSessionLog();
            case 10 -> {
                SessionManager.logout();
                System.out.println("Logout realizado.");
            }
            case 0 -> { return false; }
            default -> System.out.println("Opcao invalida.");
        }
        return true;
    }

    private void handleLogin() {
        String document = readString("CPF/CNPJ: ");
        String password = readString("Senha: ");

        try {
            Client client = clientRepository.findByDocument(document)
                    .orElseThrow(() -> new InvalidOperationException("Cliente nao encontrado."));

            if (!client.getPassword().equals(password)) {
                throw new InvalidOperationException("Senha incorreta.");
            }

            SessionManager.login(client);
            sessionLog.add("Login: " + document);
            System.out.println("Login realizado com sucesso.");

        } catch (InvalidOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleRegister() {
        try {
            String name = readString("Nome: ");
            String cpf = readString("CPF: ");
            String password = readString("Senha: ");
            String state = readString("Estado: ");
            String city = readString("Cidade: ");
            String street = readString("Rua: ");
            int number = readInt("Numero: ");

            Address address = new Address(state, city, street, number);
            clientService.createNaturalPersonClient(password, address, name, cpf);

            sessionLog.add("Cadastro: " + cpf);
            System.out.println("Cliente cadastrado. Faca login para continuar.");

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleOpenCurrentAccount() {
        try {
            Account account = accountService.createCurrentAccount();
            sessionLog.add("Conta corrente aberta: " + account.getAccountNumber());
            System.out.println("Conta corrente aberta. Numero: " + account.getAccountNumber());
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleOpenSavingsAccount() {
        try {
            Account account = accountService.createSavingsAccount();
            sessionLog.add("Conta poupanca aberta: " + account.getAccountNumber());
            System.out.println("Conta poupanca aberta. Numero: " + account.getAccountNumber());
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleDeposit() {
        try {
            int accountNumber = readInt("Numero da conta: ");
            Account account = findAccountOrThrow(accountNumber);

            double amount = readDouble("Valor: ");
            if (amount <= 0) throw new InvalidOperationException("O valor deve ser positivo.");

            account.depositMoney(BigDecimal.valueOf(amount));
            sessionLog.add(String.format("Deposito de R$ %.2f na conta %d", amount, accountNumber));
            System.out.printf("Deposito realizado. Saldo atual: R$ %.2f%n", account.getBalance().doubleValue());

        } catch (InvalidOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleWithdraw() {
        try {
            int accountNumber = readInt("Numero da conta: ");
            Account account = findAccountOrThrow(accountNumber);

            double amount = readDouble("Valor: ");
            if (amount <= 0) throw new InvalidOperationException("O valor deve ser positivo.");

            executeWithdraw(account, amount);

            sessionLog.add(String.format("Saque de R$ %.2f da conta %d", amount, accountNumber));
            System.out.printf("Saque realizado. Saldo atual: R$ %.2f%n", account.getBalance().doubleValue());

        } catch (InsufficientFundsException e) {
            System.out.println("Saldo insuficiente: " + e.getMessage());
        } catch (InvalidOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleTransfer() {
        try {
            int fromNumber = readInt("Conta de origem: ");
            int toNumber = readInt("Conta de destino: ");

            if (fromNumber == toNumber) {
                throw new InvalidOperationException("Origem e destino nao podem ser iguais.");
            }

            Account from = findAccountOrThrow(fromNumber);
            Account to = findAccountOrThrow(toNumber);

            double amount = readDouble("Valor: ");
            if (amount <= 0) throw new InvalidOperationException("O valor deve ser positivo.");

            executeWithdraw(from, amount);

            to.depositMoney(BigDecimal.valueOf(amount));
            sessionLog.add(String.format("Transferencia de R$ %.2f: %d -> %d", amount, fromNumber, toNumber));
            System.out.printf("Transferencia de R$ %.2f realizada.%n", amount);

        } catch (InsufficientFundsException e) {
            System.out.println("Saldo insuficiente: " + e.getMessage());
        } catch (InvalidOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void executeWithdraw(Account account, double amount) throws InsufficientFundsException {
        boolean success = account.withdrawMoney(BigDecimal.valueOf(amount));
        if (!success) {
            throw new InsufficientFundsException(amount, account.getBalance().doubleValue());
        }
    }

    private void handleBalance() {
        try {
            int accountNumber = readInt("Numero da conta: ");
            Account account = findAccountOrThrow(accountNumber);

            System.out.println("Conta: " + account.getAccountNumber());
            System.out.println("Agencia: " + account.getBranchNumber());
            System.out.printf("Saldo: R$ %.2f%n", account.getBalance().doubleValue());
            System.out.println("Status: " + (account.getClientOwner().isVip() ? "VIP" : "Padrao"));

        } catch (InvalidOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleLoanSimulation() {
        try {
            double amount = readDouble("Valor do emprestimo: ");
            int months = readInt("Prazo em meses: ");

            double[][] table;

            Client client = SessionManager.getLoggedInClient().orElseThrow();
            if (client.isVip()) {
                System.out.println("Taxa VIP aplicada.");
                table = loanService.simulateSAC(amount, months);
            } else {
                double rate = readDouble("Taxa de juros mensal (ex: 0.015): ");
                table = loanService.simulateSAC(amount, months, rate);
            }

            System.out.printf("%-6s %-12s %-12s %-12s %-12s%n",
                    "Parc.", "Prestacao", "Amortizacao", "Juros", "Saldo Dev.");

            for (double[] row : table) {
                System.out.printf("%-6.0f %-12.2f %-12.2f %-12.2f %-12.2f%n",
                        row[0], row[1], row[2], row[3], row[4]);
            }

            sessionLog.add(String.format("Simulacao SAC: R$ %.2f em %d meses", amount, months));

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void handleTotalOfAccounts() {
        int total = Account.getTotalOfAccounts();

        System.out.println("\n--- Resumo do Sistema ---");
        System.out.println("Total de contas armazenadas: " + total);

        sessionLog.add("Consulta do total de contas (Total: " + total + ")");
    }

    private void handleSessionLog() {
        if (sessionLog.isEmpty()) {
            System.out.println("Nenhuma operacao registrada.");
            return;
        }

        for (int i = 0; i < sessionLog.size(); i++) {
            System.out.println((i + 1) + ". " + sessionLog.get(i));
        }
    }

    private Account findAccountOrThrow(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidOperationException("Conta " + accountNumber + " nao encontrada."));
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um valor numerico.");
            }
        }
    }

    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Este campo nao pode ser vazio.");
        }
    }
}