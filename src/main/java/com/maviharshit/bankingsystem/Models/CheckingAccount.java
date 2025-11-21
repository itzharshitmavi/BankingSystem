package com.maviharshit.bankingsystem.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account{
    private final IntegerProperty transactionLimit;
    public CheckingAccount(String owner, String accountNumber, double balance, int tLimit){
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "Transaction Limit", tLimit);
    }
    public int getTransactionLimit() {
        return transactionLimit.get();
    }

    @Override
    public String getAccountNumber() {
        return super.getAccountNumber();
    }

    public IntegerProperty transactionLimitProperty(){return transactionLimit;}

}
