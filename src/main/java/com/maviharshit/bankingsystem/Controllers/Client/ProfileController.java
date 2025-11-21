package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private Label fullName_lbl;
    @FXML private Label payeeAddress_lbl;
    @FXML private Label accountCreated_lbl;
    @FXML private Label checkingAccNum_lbl;
    @FXML private Label checkingBal_lbl;
    @FXML private Label savingAccNum_lbl;
    @FXML private Label savingBal_lbl;
    @FXML private Label transactionLimit_lbl;
    @FXML private Label withdrawalLimit_lbl;
    @FXML private Label lastLogin_lbl;
    @FXML private Button updatePassword_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProfileData();

        updatePassword_btn.setOnAction(event -> {
            // TODO: Open update password dialog or screen
            System.out.println("Update Password clicked");
        });
    }

    private void loadProfileData() {
        Client client = Model.getInstance().getClient();

        fullName_lbl.setText(client.getFirstName() + " " + client.getLastName());
        payeeAddress_lbl.setText(client.getPayeeAddress());

        LocalDate creationDate = client.getDateCreated();
        accountCreated_lbl.setText(creationDate != null ? creationDate.toString() : "N/A");

        if (client.getCheckingAccount() != null) {
            checkingAccNum_lbl.setText(client.getCheckingAccount().getAccountNumber());
            checkingBal_lbl.setText(String.format("₹%.2f", client.getCheckingAccount().getBalance()));
            transactionLimit_lbl.setText(String.format("₹%.2f", (double)client.getCheckingAccount().getTransactionLimit()));
        } else {
            checkingAccNum_lbl.setText("N/A");
            checkingBal_lbl.setText("N/A");
            transactionLimit_lbl.setText("N/A");
        }

        if (client.getSavingAccount() != null) {
            savingAccNum_lbl.setText(client.getSavingAccount().getAccountNumber());
            savingBal_lbl.setText(String.format("₹%.2f", client.getSavingAccount().getBalance()));
            withdrawalLimit_lbl.setText(String.format("₹%.2f", (double)client.getSavingAccount().getWithdrawalLimit()));
        } else {
            savingAccNum_lbl.setText("N/A");
            savingBal_lbl.setText("N/A");
            withdrawalLimit_lbl.setText("N/A");
        }

        // If you track last login date somewhere, set it here; otherwise dummy
        lastLogin_lbl.setText("N/A");
    }
}
