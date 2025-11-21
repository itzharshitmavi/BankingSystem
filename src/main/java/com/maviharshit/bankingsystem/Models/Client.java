package com.maviharshit.bankingsystem.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final StringProperty password;
    private final ObjectProperty<CheckingAccount> checkingAccount;
    private final ObjectProperty<SavingsAccount> savingAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    public CheckingAccount getCheckingAccount() {
        return checkingAccount.get();
    }

    public SavingsAccount getSavingAccount() {
        return savingAccount.get();
    }

    public Client(String firstName, String lastName, String payeeAddress, CheckingAccount checkingAccount, SavingsAccount savingAccount, LocalDate dateCreated, String password) {
        this.firstName = new SimpleStringProperty(this, "FirstName", firstName);
        this.lastName = new SimpleStringProperty(this, "LastName", lastName);
        this.payeeAddress = new SimpleStringProperty(this, "Payee Address", payeeAddress);
        this.checkingAccount = new SimpleObjectProperty<CheckingAccount>(this, "Checking Account", checkingAccount);
        this.savingAccount = new SimpleObjectProperty<SavingsAccount>(this, "Saving Account", savingAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "Date", dateCreated);
        this.password = new SimpleStringProperty(this, "Password", password);
    }

    public StringProperty firstNameProperty(){return firstName;}
    public StringProperty lastNameProperty(){return lastName;}
    public StringProperty pAddressProperty(){return payeeAddress;}

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getPayeeAddress() {
        return payeeAddress.get();
    }

    public StringProperty payeeAddressProperty() {
        return payeeAddress;
    }

    public String getPassword() {
        return password.get();
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }

    public StringProperty passwordProperty(){return password;}
    public ObjectProperty<CheckingAccount> checkingAccountProperty(){return checkingAccount;}
    public ObjectProperty<SavingsAccount> savingAccountProperty(){return savingAccount;}
    public ObjectProperty<LocalDate> dateCreatedProperty(){return dateCreated;}

}
