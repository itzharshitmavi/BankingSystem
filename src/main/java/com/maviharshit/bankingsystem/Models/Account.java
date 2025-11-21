package com.maviharshit.bankingsystem.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account {
    private final StringProperty owner;

    private final StringProperty accountNumber;
    private final DoubleProperty balance;

    public Account(String owner, String accountNumber, double balance){
        this.owner = new SimpleStringProperty(this, "owner", owner);
        this.accountNumber = new SimpleStringProperty(this, "Account Number", accountNumber);
        this.balance = new SimpleDoubleProperty(this, "Balance", balance);
    }
    public String getAccountNumber() {
        return accountNumber.get();
    }

    public String getOwner() {
        return owner.get();
    }

    public double getBalance() {
        return balance.get();
    }
    public StringProperty ownerProperty(){return owner;}
    public StringProperty accountNumberProperty(){return accountNumber;}
    public DoubleProperty balanceProperty(){return balance;}
}
