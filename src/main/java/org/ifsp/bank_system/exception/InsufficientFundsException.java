package org.ifsp.bank_system.exception;

public class InsufficientFundsException extends Exception {

    private final double attemptedAmount;
    private final double availableBalance;

    public InsufficientFundsException(double attemptedAmount, double availableBalance) {
        super(String.format(
            "Saldo insuficiente. Tentativa: R$ %.2f | Disponível: R$ %.2f",
            attemptedAmount, availableBalance
        ));
        this.attemptedAmount = attemptedAmount;
        this.availableBalance = availableBalance;
    }

    public double getAttemptedAmount() {
        return attemptedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
