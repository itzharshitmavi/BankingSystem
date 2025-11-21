package com.maviharshit.bankingsystem.Models;

import com.maviharshit.bankingsystem.Views.AccountType;
import com.maviharshit.bankingsystem.Views.BankAccountType;
import com.maviharshit.bankingsystem.Views.ViewFactory;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.CLIENT;
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private boolean adminLoginSuccessFlag = false;
    private Model(){
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.clientLoginSuccessFlag = false;
        this.client = new Client("","","", null, null, null, "");
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){return viewFactory;}
    public DatabaseDriver getDatabaseDriver(){return databaseDriver;}
    public AccountType getLoginAccountType() {return loginAccountType;}
    public void setLoginAccountType(AccountType loginAccountType) {this.loginAccountType = loginAccountType;}
    public boolean getClientLoginSuccessFlag(){return this.clientLoginSuccessFlag;}
    public boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public void setClientLoginSuccessFlag(boolean flag){this.clientLoginSuccessFlag = flag;}
    public Client getClient() {return client;}

    public void evaluateClientCred(String pAddress, String password){
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress, password);
        try {
            if(resultSet.isBeforeFirst()){
                this.client.firstNameProperty().set((resultSet.getString("FirstName")));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.pAddressProperty().set((resultSet.getString("PayeeAddress")));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                this.clientLoginSuccessFlag = true;
                loadClientAccounts(pAddress);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void evaluateAdminCred(String username, String password){
        ResultSet rs = databaseDriver.getAdminData(username, password);

        this.adminLoginSuccessFlag = false; // reset before checking

        try {
            if (rs != null && rs.isBeforeFirst()) {
                // admin found → login success
                this.adminLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean createNewClient(String f, String l, String pAddress, String pass, String chAccNum, String svAccNum,
                                   boolean hasChecking, boolean hasSaving,
                                   double chAmount, double svAmount) {

        boolean ok = databaseDriver.insertClient(f, l, pAddress, pass);
        if (!ok) return false;

        if (hasChecking) {
            databaseDriver.insertCheckingAccount(pAddress, chAmount, chAccNum);
        }

        if (hasSaving) {
            databaseDriver.insertSavingsAccount(pAddress, svAmount, svAccNum);
        }

        return true;
    }
    public List<Transaction> getTransactionHistoryForClient(Client client) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String pAddress = client.pAddressProperty().get();
            ResultSet rs = databaseDriver.getTransactionsForClient(pAddress);

            while (rs.next()) {
                String sender = rs.getString("Sender");
                String receiver = rs.getString("Receiver");
                double amount = rs.getDouble("Amount");
                LocalDate date = LocalDate.parse(rs.getString("Date"));
                String message = rs.getString("Message");

                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Send money implementation: debit from sender, credit to receiver, add transaction record
    public boolean sendMoneyTo(String payeeAddress, double amount, String message) {
        try {
            String senderAddress = getClient().pAddressProperty().get();

            // Validate sender balance
            double senderBalance = databaseDriver.getBalanceForClient(senderAddress);
            if (senderBalance < amount) {
                return false; // insufficient balance
            }

            // Begin transaction (pseudo-code, implement actual db transaction)

            // Debit sender checking account
            boolean debitSuccess = databaseDriver.updateBalance(senderAddress, senderBalance - amount);
            if (!debitSuccess) return false;

            // Credit receiver checking account
            double receiverBalance = databaseDriver.getBalanceForClient(payeeAddress);
            boolean creditSuccess = databaseDriver.updateBalance(payeeAddress, receiverBalance + amount);
            if (!creditSuccess) return false;

            // Insert transaction record
            boolean txSuccess = databaseDriver.insertTransaction(senderAddress, payeeAddress, amount, LocalDate.now(), message);
            if (!txSuccess) return false;

            // Commit transaction

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback transaction if applicable
            return false;
        }
    }
    public void loadClientAccounts(String pAddress) {
        try {
            // Load checking account
            ResultSet rsCheck = databaseDriver.getCheckingAccountByOwner(pAddress);
            CheckingAccount checkingAccount = null;
            if (rsCheck != null && rsCheck.next()) {
                String chAccNum = rsCheck.getString("AccountNumber");
                double chBalance = rsCheck.getDouble("Balance");
                int chLimit = rsCheck.getInt("TransactionLimit");
                checkingAccount = new CheckingAccount(pAddress, chAccNum, chBalance, chLimit);
            }
            client.checkingAccountProperty().set(checkingAccount);

            // Load savings account
            ResultSet rsSave = databaseDriver.getSavingsAccountByOwner(pAddress);
            SavingsAccount savingsAccount = null;
            if (rsSave != null && rsSave.next()) {
                String svAccNum = rsSave.getString("AccountNumber");
                double svBalance = rsSave.getDouble("Balance");
                int svLimit = rsSave.getInt("WithdrawalLimit");
                savingsAccount = new SavingsAccount(pAddress, svAccNum, svBalance, svLimit);
            }
            client.savingAccountProperty().set(savingsAccount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean transferBetweenOwnAccounts(double amount, boolean fromCheckingToSavings) {
        try {
            Client client = getClient();
            CheckingAccount checking = client.getCheckingAccount();
            SavingsAccount savings = client.getSavingAccount();

            if (checking == null || savings == null) return false;

            if (amount <= 0) return false;

            if (fromCheckingToSavings) {
                // Check sufficient balance in checking account
                if (checking.getBalance() < amount) return false;

                double newCheckingBal = checking.getBalance() - amount;
                double newSavingsBal = savings.getBalance() + amount;

                // Update balances in DB
                if (!databaseDriver.updateBalance(checking.getOwner(), newCheckingBal)) return false;
                if (!databaseDriver.updateSavingsBalance(savings.getOwner(), newSavingsBal)) return false;

                // Update local model properties
                checking.balanceProperty().set(newCheckingBal);
                savings.balanceProperty().set(newSavingsBal);

            } else {
                // Transfer from savings to checking
                if (savings.getBalance() < amount) return false;

                double newSavingsBal = savings.getBalance() - amount;
                double newCheckingBal = checking.getBalance() + amount;

                if (!databaseDriver.updateSavingsBalance(savings.getOwner(), newSavingsBal)) return false;
                if (!databaseDriver.updateBalance(checking.getOwner(), newCheckingBal)) return false;

                checking.balanceProperty().set(newCheckingBal);
                savings.balanceProperty().set(newSavingsBal);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean sendMoneyToAccount(String receiverPayee, String receiverAccountNum, double amount, String message, BankAccountType senderAccountType) {
        Client sender = getClient();
        if (sender == null) return false;

        // Basic validation and use of DB driver to update balances and create transaction record
        if (senderAccountType == BankAccountType.CHECKING) {
            if (sender.getCheckingAccount() == null || sender.getCheckingAccount().getBalance() < amount) return false;
            return databaseDriver.transferFromCheckingToAccount(sender.getPayeeAddress(), receiverPayee, receiverAccountNum, amount, message);
        } else {
            if (sender.getSavingAccount() == null || sender.getSavingAccount().getBalance() < amount) return false;
            return databaseDriver.transferFromSavingToAccount(sender.getPayeeAddress(), receiverPayee, receiverAccountNum, amount, message);
        }
    }

}
