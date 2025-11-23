package com.maviharshit.bankingsystem.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;
    public DatabaseDriver(){
        try{
            this.conn = DriverManager.getConnection("jdbc:sqlite:Bank.db");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet getClientData(String pAddress, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"' AND Password='"+password+"';");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getAdminData(String username, String password){
        ResultSet resultSet = null;
        try{
            Statement statement = this.conn.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM Admins WHERE Username='"+username+"' AND Password='"+password+"';"
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public boolean insertClient(String fname, String lname, String payee, String password) {
        String query = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, date('now'))";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, payee);
            stmt.setString(4, password);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertCheckingAccount(String clientId, double amount, String accountNumber) {
        String query = "INSERT INTO CheckingAccounts (Owner, Balance, AccountNumber, TransactionLimit) VALUES (?, ?, ?, 10000)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, clientId);
            stmt.setDouble(2, amount);
            stmt.setString(3, accountNumber);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertSavingsAccount(String clientId, double amount, String accountNumber) {
        String query = "INSERT INTO SavingsAccounts (Owner, Balance, AccountNumber, WithdrawalLimit) VALUES (?, ?, ?, 5000)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, clientId);
            stmt.setDouble(2, amount);
            stmt.setString(3, accountNumber);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSet getAllClients() {
        ResultSet resultSet = null;
        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public boolean deleteClient(String payeeAddress) {
        try {
            Statement statement = conn.createStatement();
            int rows = statement.executeUpdate("DELETE FROM Clients WHERE PayeeAddress='" + payeeAddress + "';");
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ResultSet getClientsByPayeeAddress(String payeeAddress) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress LIKE '%" + payeeAddress + "%';";
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean depositToClientChecking(String payeeAddress, double amount) {
        try {
            // Simplified: You should first read current balance, then update it
            Statement statement = conn.createStatement();
            // Example SQL to add amount to checking account balance (assumes such table/field exist)
            String update = "UPDATE CheckingAccounts SET Balance = Balance + " + amount + " WHERE Owner = '" + payeeAddress + "';";
            int updatedRows = statement.executeUpdate(update);
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ResultSet getTransactionsForClient(String payeeAddress) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Transactions WHERE Sender = '" + payeeAddress + "' OR Receiver = '" + payeeAddress + "' ORDER BY Date DESC;";
            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getBalanceForClient(String payeeAddress) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT Balance FROM CheckingAccounts WHERE Owner = '" + payeeAddress + "';";
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getDouble("Balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean updateBalance(String payeeAddress, double newBalance) {
        try {
            Statement statement = conn.createStatement();
            String update = "UPDATE CheckingAccounts SET Balance = " + newBalance + " WHERE Owner = '" + payeeAddress + "';";
            int rowsUpdated = statement.executeUpdate(update);
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertTransaction(String sender, String receiver, double amount, LocalDate date, String message) {
        try {
            Statement statement = conn.createStatement();
            String insert = String.format("INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) " +
                    "VALUES ('%s', '%s', %f, '%s', '%s');", sender, receiver, amount, date.toString(), message);
            int rowsInserted = statement.executeUpdate(insert);
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ResultSet getCheckingAccountByOwner(String owner) {
        try {
            String query = "SELECT * FROM CheckingAccounts WHERE Owner = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, owner);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getSavingsAccountByOwner(String owner) {
        try {
            String query = "SELECT * FROM SavingsAccounts WHERE Owner = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, owner);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return this.conn;
    }
    public boolean updateSavingsBalance(String owner, double newBalance) {
        try {
            String update = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setDouble(1, newBalance);
            stmt.setString(2, owner);
            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean depositToClientSaving(String payeeAddress, double amount) {
        try {
            String update = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setDouble(1, amount);
            stmt.setString(2, payeeAddress);
            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean transferFromCheckingToAccount(String senderPayeeAddress, String receiverPayeeAddress, String receiverAccountNumber, double amount, String message) {
        try {
            conn.setAutoCommit(false);

            // Check sender's checking balance
            double senderBalance = getBalance("CheckingAccounts", senderPayeeAddress);
            if (senderBalance < amount) {
                conn.rollback();
                return false;
            }

            // Identify whether receiver's account number is checking or saving
            String receiverTable = findAccountTableByAccountNumber(receiverAccountNumber);
            if (receiverTable == null) {
                conn.rollback();
                return false;
            }

            // Deduct from sender checking
            if (!updateBalance("CheckingAccounts", senderPayeeAddress, senderBalance - amount)) {
                conn.rollback();
                return false;
            }

            // Credit to receiver account
            double receiverBalance = getBalance(receiverTable, receiverAccountNumber);
            if (!updateBalanceByAccountNumber(receiverTable, receiverAccountNumber, receiverBalance + amount)) {
                conn.rollback();
                return false;
            }

            String ownerSql = "SELECT Owner FROM " + receiverTable + " WHERE AccountNumber = ?";
            PreparedStatement ownerStmt = conn.prepareStatement(ownerSql);
            ownerStmt.setString(1, receiverAccountNumber);
            ResultSet ownerRs = ownerStmt.executeQuery();
            if (!ownerRs.next() || !ownerRs.getString("Owner").equals(receiverPayeeAddress)) {
                ownerRs.close();
                ownerStmt.close();
                conn.rollback();
                return false; // Mismatched owner and account number
            }
            ownerRs.close();
            ownerStmt.close();
            // ==========================


            // Log transaction
            insertTransaction(senderPayeeAddress, receiverPayeeAddress, amount, message);

            conn.commit();
            return true;

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // Similar method for saving account as sender
    public boolean transferFromSavingToAccount(String senderPayeeAddress, String receiverPayeeAddress, String receiverAccountNumber, double amount, String message) {
        try {
            conn.setAutoCommit(false);

            // Check sender's saving balance
            double senderBalance = getBalance("SavingsAccounts", senderPayeeAddress);
            if (senderBalance < amount) {
                conn.rollback();
                return false;
            }

            String receiverTable = findAccountTableByAccountNumber(receiverAccountNumber);
            if (receiverTable == null) {
                conn.rollback();
                return false;
            }

            // Deduct from sender saving
            if (!updateBalance("SavingsAccounts", senderPayeeAddress, senderBalance - amount)) {
                conn.rollback();
                return false;
            }

            // Credit to receiver account
            double receiverBalance = getBalance(receiverTable, receiverAccountNumber);
            if (!updateBalanceByAccountNumber(receiverTable, receiverAccountNumber, receiverBalance + amount)) {
                conn.rollback();
                return false;
            }
            String ownerSql = "SELECT Owner FROM " + receiverTable + " WHERE AccountNumber = ?";
            PreparedStatement ownerStmt = conn.prepareStatement(ownerSql);
            ownerStmt.setString(1, receiverAccountNumber);
            ResultSet ownerRs = ownerStmt.executeQuery();
            if (!ownerRs.next() || !ownerRs.getString("Owner").equals(receiverPayeeAddress)) {
                ownerRs.close();
                ownerStmt.close();
                conn.rollback();
                return false; // Mismatched owner and account number
            }
            ownerRs.close();
            ownerStmt.close();
            // ==========================


            insertTransaction(senderPayeeAddress, receiverPayeeAddress, amount, message);

            conn.commit();
            return true;

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private double getBalance(String tableName, String key) throws SQLException {
        String column = tableName.equals("CheckingAccounts") || tableName.equals("SavingsAccounts") ? "Owner" : "AccountNumber";
        String sql = "SELECT Balance FROM " + tableName + " WHERE " + column + " = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getDouble("Balance");
        }
        return 0.0;
    }

    private boolean updateBalance(String tableName, String owner, double newBalance) throws SQLException {
        String sql = "UPDATE " + tableName + " SET Balance = ? WHERE Owner = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, newBalance);
        stmt.setString(2, owner);
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    private boolean updateBalanceByAccountNumber(String tableName, String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE " + tableName + " SET Balance = ? WHERE AccountNumber = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, newBalance);
        stmt.setString(2, accountNumber);
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    private String findAccountTableByAccountNumber(String accountNumber) throws SQLException {
        String checkSql = "SELECT COUNT(*) AS cnt FROM CheckingAccounts WHERE AccountNumber = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, accountNumber);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt("cnt") > 0) {
            return "CheckingAccounts";
        }
        rs.close();
        checkStmt.close();

        String saveSql = "SELECT COUNT(*) AS cnt FROM SavingsAccounts WHERE AccountNumber = ?";
        PreparedStatement saveStmt = conn.prepareStatement(saveSql);
        saveStmt.setString(1, accountNumber);
        rs = saveStmt.executeQuery();
        if (rs.next() && rs.getInt("cnt") > 0) {
            rs.close();
            saveStmt.close();
            return "SavingsAccounts";
        }
        rs.close();
        saveStmt.close();

        return null; // Not found
    }

    private void insertTransaction(String sender, String receiver, double amount, String message) throws SQLException {
        String sql = "INSERT INTO Transactions(Sender, Receiver, Amount, Message, Date) VALUES (?, ?, ?, ?, date('now'))";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, sender);
        stmt.setString(2, receiver);
        stmt.setDouble(3, amount);
        stmt.setString(4, message);
        stmt.executeUpdate();
        stmt.close();
    }
    public boolean validateClientPassword(String payeeAddress, String password) {
        try {
            ResultSet rs = getClientData(payeeAddress, password);
            return (rs != null && rs.isBeforeFirst());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClientPassword(String payeeAddress, String newPassword) {
        try {
            String sql = "UPDATE Clients SET Password=? WHERE PayeeAddress=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, payeeAddress);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertBugReport(String payeeAddress, String description) {
        try {
            String sql = "INSERT INTO BugReports (payee_address, description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, payeeAddress);
            stmt.setString(2, description);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllBugReports() {
        try {
            String sql = "SELECT * FROM BugReports ORDER BY id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
