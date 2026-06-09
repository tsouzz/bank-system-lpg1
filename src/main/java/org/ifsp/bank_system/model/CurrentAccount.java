package org.ifsp.bank_system.model;

import java.math.BigDecimal;

public final class CurrentAccount extends Account {

    private static final BigDecimal INITIAL_OVERDRAFT_LIMIT = BigDecimal.valueOf(500.00);

    private BigDecimal overDraftLimit;

    public CurrentAccount(Client clientOwner) {
        super(clientOwner);
        this.overDraftLimit = INITIAL_OVERDRAFT_LIMIT;
    }

    public BigDecimal getOverDraftLimit() {
        return overDraftLimit;
    }

    @Override
    public boolean withdrawMoney(BigDecimal amount) {
        BigDecimal availableLimit = getBalance().add(getOverDraftLimit());

        if (amount.compareTo(availableLimit) > 0) {
            return false;
        }

        setBalance(getBalance().subtract(amount));
        return true;
    }
}
