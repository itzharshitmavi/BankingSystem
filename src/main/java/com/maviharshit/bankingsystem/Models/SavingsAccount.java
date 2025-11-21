package com.maviharshit.bankingsystem.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SavingsAccount extends Account{
    private final IntegerProperty withdrawalLimit;
    public SavingsAccount(String owner, String accountNumber, double balance, int wLimit){
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleIntegerProperty(this, "Withdrawal Limit", wLimit);
    }
    public int getWithdrawalLimit() {
        return withdrawalLimit.get();
    }

    @Override
    public String getAccountNumber() {
        return super.getAccountNumber();
    }

    public IntegerProperty withdrawalLimitProperty(){return withdrawalLimit;}
}

