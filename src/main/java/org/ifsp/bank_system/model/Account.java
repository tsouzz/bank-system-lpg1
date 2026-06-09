package org.ifsp.bank_system.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public abstract class Account {

    private static int totalOfAccounts = 0;
    private static final String DEFAULT_BRANCH = "0001";
    private static int accountCounter = 1000;

    private final String branchNumber;
    private final int accountNumber;
    private BigDecimal balance;
    private final Client clientOwner;

    public Account(Client clientOwner) {
        this.branchNumber = DEFAULT_BRANCH;
        this.accountNumber = ++accountCounter;
        this.balance = BigDecimal.ZERO;
        this.clientOwner = clientOwner;

        totalOfAccounts++;
    }

    public static int getTotalOfAccounts() {
        return totalOfAccounts;
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public String getBranchNumber() {
        return branchNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Client getClientOwner() {
        return clientOwner;
    }

    public abstract boolean withdrawMoney(BigDecimal amount);

    public final void depositMoney(BigDecimal amount) {
        setBalance(getBalance().add(amount));
        checkVip();
    }

    public void checkVip(){

        OffsetDateTime aYearAgo = OffsetDateTime.now().minusYears(1);

        boolean hasSufficientBalance = this.getBalance().compareTo(BigDecimal.valueOf(5000)) >= 0;

        boolean hasAccountTime = !clientOwner.getCreatedAt().isAfter(aYearAgo);

        if(hasSufficientBalance && hasAccountTime){
            this.clientOwner.setVip(true);
        }
    }
}
