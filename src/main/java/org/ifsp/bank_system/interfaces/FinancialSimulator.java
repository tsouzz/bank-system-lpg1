package org.ifsp.bank_system.interfaces;

public interface FinancialSimulator {

    double[][] simulateSAC(double amount, int months, double interestRate);

}