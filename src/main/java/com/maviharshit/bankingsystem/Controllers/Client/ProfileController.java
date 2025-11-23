package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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
    @FXML private Button updatePassword_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProfileData();

        updatePassword_btn.setOnAction(event -> showChangePasswordDialog());
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
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");

        ButtonType updateButtonType = new ButtonType("Update Password", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        PasswordField oldPasswordFld = new PasswordField();
        oldPasswordFld.setPromptText("Old Password");
        PasswordField newPasswordFld = new PasswordField();
        newPasswordFld.setPromptText("New Password");
        PasswordField confirmPasswordFld = new PasswordField();
        confirmPasswordFld.setPromptText("Confirm New Password");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        grid.add(new Label("Old Password:"), 0, 0); grid.add(oldPasswordFld, 1, 0);
        grid.add(new Label("New Password:"), 0, 1); grid.add(newPasswordFld, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2); grid.add(confirmPasswordFld, 1, 2);
        dialog.getDialogPane().setContent(grid);

        // Enable update only if new & confirm match and not empty
        Button updateBtn = (Button) dialog.getDialogPane().lookupButton(updateButtonType);
        updateBtn.setDisable(true);

        Runnable validator = () -> {
            boolean disable = newPasswordFld.getText().isEmpty()
                    || confirmPasswordFld.getText().isEmpty()
                    || !newPasswordFld.getText().equals(confirmPasswordFld.getText());
            updateBtn.setDisable(disable);
        };
        newPasswordFld.textProperty().addListener((obs, oldVal, newVal) -> validator.run());
        confirmPasswordFld.textProperty().addListener((obs, oldVal, newVal) -> validator.run());

        dialog.setResultConverter(button -> button);

        dialog.showAndWait().ifPresent(button -> {
            if (button == updateButtonType) {
                processPasswordChange(oldPasswordFld.getText(), newPasswordFld.getText());
            }
        });
    }


    private void processPasswordChange(String oldPass, String newPass) {
        // Example validation; replace with your DB/user logic!
        String clientPayee = Model.getInstance().getClient().getPayeeAddress();
        boolean valid = Model.getInstance().getDatabaseDriver().validateClientPassword(clientPayee, oldPass); // Implement this in your DB driver
        if (!valid) {
            showAlert("Old password is incorrect.");
            return;
        }
        boolean updated = Model.getInstance().getDatabaseDriver().updateClientPassword(clientPayee, newPass); // Implement this in your DB driver
        if (updated) {
            showAlert("Password updated successfully!");
        } else {
            showAlert("Failed to update password.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
