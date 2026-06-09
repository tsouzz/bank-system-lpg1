package org.ifsp.bank_system.service;

import org.ifsp.bank_system.interfaces.FinancialSimulator;

public class LoanService implements FinancialSimulator {

    private static final double VIP_INTEREST_RATE = 0.008;

    public double[][] simulateSAC(double amount, int months) {
        return simulateSAC(amount, months, VIP_INTEREST_RATE);
    }

    @Override
    public double[][] simulateSAC(double amount, int months, double interestRate) {

        if (amount <= 0 || months <= 0) {
            throw new IllegalArgumentException("Valor e meses devem ser maiores que zero.");
        }

        double[][] sacTable = new double[months][5];

        double amortization = amount / months;
        double outstandingBalance = amount;

        for (int i = 0; i < months; i++) {
            double interest = outstandingBalance * interestRate;
            double installment = amortization + interest;
            outstandingBalance -= amortization;

            if (outstandingBalance < 0.01) {
                outstandingBalance = 0.0;
            }

            sacTable[i][0] = i + 1;
            sacTable[i][1] = installment;
            sacTable[i][2] = amortization;
            sacTable[i][3] = interest;
            sacTable[i][4] = outstandingBalance;
        }

        return sacTable;
    }
}