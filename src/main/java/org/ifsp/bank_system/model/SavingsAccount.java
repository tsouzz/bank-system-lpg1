package org.ifsp.bank_system.model;

import java.math.BigDecimal;

public final class SavingsAccount extends Account{

    private final static float INTEREST_RATE = 0.005f;

    public SavingsAccount(Client clientOwner) {
        super(clientOwner);
    }

    @Override
    public boolean withdrawMoney(BigDecimal amount) {
        if (amount.compareTo(getBalance()) > 0) {
            return false;
        }

        setBalance(getBalance().subtract(amount));
        return true;
    }
}