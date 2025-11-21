package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    @FXML
    public TextField amount_to_ch;

    @FXML
    public TextField amount_to_sv;

    @FXML
    public Label ch_acc_num;

    @FXML
    public Label ch_account_bal;

    @FXML
    public Label ch_account_date;

    @FXML
    public Label sv_acc_bal;

    @FXML
    public Label sv_acc_date;

    @FXML
    public Label sv_acc_num;

    @FXML
    public Button trans_to_ch_btn;

    @FXML
    public Button trans_to_sv_btn;

    @FXML
    public Label transaction_limit;

    @FXML
    public Label withdrawal_limit;
    @FXML
    public Label transfer_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trans_to_ch_btn.setOnAction(e -> handleTransferToChecking());

        trans_to_sv_btn.setOnAction(e -> handleTransferToSavings());
        loadAccountDetails();
    }
    private void loadAccountDetails() {
        Client client = Model.getInstance().getClient();

        if (client.checkingAccountProperty().get() != null) {
            ch_acc_num.setText(client.checkingAccountProperty().get().getAccountNumber());
            ch_account_bal.setText(String.format("₹%.2f", client.checkingAccountProperty().get().getBalance()));
            transaction_limit.setText("₹" + client.checkingAccountProperty().get().getTransactionLimit());
        } else {
            ch_acc_num.setText("N/A");
            ch_account_bal.setText("N/A");
            transaction_limit.setText("N/A");
        }

        if (client.savingAccountProperty().get() != null) {
            sv_acc_num.setText(client.savingAccountProperty().get().getAccountNumber());
            sv_acc_bal.setText(String.format("₹%.2f", client.savingAccountProperty().get().getBalance()));
            withdrawal_limit.setText("₹" + client.savingAccountProperty().get().getWithdrawalLimit());
        } else {
            sv_acc_num.setText("N/A");
            sv_acc_bal.setText("N/A");
            withdrawal_limit.setText("N/A");
        }

        transfer_lbl.setText("");
    }
    private void handleTransferToChecking() {
        transfer_lbl.setText("");
        String amountStr = amount_to_ch.getText().trim();

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                transfer_lbl.setText("Enter a positive amount to transfer.");
                return;
            }
        } catch (NumberFormatException e) {
            transfer_lbl.setText("Invalid amount format.");
            return;
        }

        boolean success = Model.getInstance().transferBetweenOwnAccounts(amount, false); // Savings to Checking

        if (success) {
            transfer_lbl.setText("Transferred successfully: Savings → Checking");
            amount_to_ch.clear();
            loadAccountDetails();  // Refresh balances
        } else {
            transfer_lbl.setText("Transfer failed. Check balance or try again.");
        }
    }

    private void handleTransferToSavings() {
        transfer_lbl.setText("");
        String amountStr = amount_to_sv.getText().trim();

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                transfer_lbl.setText("Enter a positive amount to transfer.");
                return;
            }
        } catch (NumberFormatException e) {
            transfer_lbl.setText("Invalid amount format.");
            return;
        }

        boolean success = Model.getInstance().transferBetweenOwnAccounts(amount, true); // Checking to Savings

        if (success) {
            transfer_lbl.setText("Transferred successfully: Checking → Savings");
            amount_to_sv.clear();
            loadAccountDetails();  // Refresh balances
        } else {
            transfer_lbl.setText("Transfer failed. Check balance or try again.");
        }
    }
}
