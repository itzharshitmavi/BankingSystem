package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ReportBugController {

    @FXML
    private TextArea bugDescription_field;

    @FXML
    private TextField payeeId_field;

    @FXML
    private Button sendReport_btn;

    @FXML
    public void initialize() {
        String payeeId = Model.getInstance().getClient().getPayeeAddress();
        payeeId_field.setText(payeeId);
        payeeId_field.setEditable(false);

        sendReport_btn.setOnAction(event -> {
            String description = bugDescription_field.getText().trim();

            if (description.isEmpty()) {
                showAlert("Please describe the bug or issue.");
                return;
            }

            boolean saved = Model.getInstance().getDatabaseDriver().insertBugReport(payeeId, description);
            if (saved) {
                showAlert("Thank you for reporting the issue!");
                bugDescription_field.setText("");
            } else {
                showAlert("Failed to save bug report! Please try again.");
                bugDescription_field.setText("");
            }
        });

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Bug");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
