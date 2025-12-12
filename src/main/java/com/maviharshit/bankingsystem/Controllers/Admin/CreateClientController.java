package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.DatabaseDriver;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    @FXML
    public CheckBox ch_acc_box;

    @FXML
    public TextField ch_amount_fld;

    @FXML
    public Button create_client_btn;

    @FXML
    public Label error_lbl;

    @FXML
    public TextField fName_fld;

    @FXML
    public TextField lName_fld;

    @FXML
    public CheckBox pAddress_fld;

    @FXML
    public Label pAddress_lbl;

    @FXML
    public PasswordField password_fld;

    @FXML
    public CheckBox sv_acc_box;

    @FXML
    public TextField sv_amount_fld;
    @FXML
    public Label ch_acc_lbl;
    @FXML
    public Label sv_acc_lbl;

    private DatabaseDriver databaseDriver;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_fld.setOnAction(event -> onGeneratePayeeClicked());
        ch_acc_box.setOnAction(event -> onGenerateCheckingClicked());
        sv_acc_box.setOnAction(event -> onGenerateSavingClicked());
        ch_amount_fld.setDisable(true);
        sv_amount_fld.setDisable(true);

        ch_acc_box.selectedProperty().addListener((obs, oldVal, newVal) -> {
            ch_amount_fld.setDisable(!newVal);
            if (!newVal) ch_amount_fld.clear();
        });

        sv_acc_box.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sv_amount_fld.setDisable(!newVal);
            if (!newVal) sv_amount_fld.clear();
        });
    }

    public CreateClientController(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }
    public CreateClientController(){
        this.databaseDriver = new DatabaseDriver();

    }

    private void onGenerateCheckingClicked(){
        if(!ch_acc_box.isSelected()){
            ch_acc_lbl.setText("");
            return;
        }
        if(fName_fld.getText().isEmpty() || lName_fld.getText().isEmpty() || password_fld.getText().isEmpty()){
            ch_acc_lbl.setText("");
            return;
        }
        String result = generateRandomCheckingAccountNumber();
        ch_acc_lbl.setText(result);
    }
    private void onGenerateSavingClicked(){
        if(!ch_acc_box.isSelected()){
            ch_acc_lbl.setText("");
            return;
        }
        if(fName_fld.getText().isEmpty() || lName_fld.getText().isEmpty() || password_fld.getText().isEmpty()){
            ch_acc_lbl.setText("");
            return;
        }
        String result = generateRandomSavingAccountNumber();
        sv_acc_lbl.setText(result);
    }

    private void onGeneratePayeeClicked() {

        if (!pAddress_fld.isSelected()) {
            pAddress_lbl.setText("");
            return;
        }

        // Get the live text from the input fields
        String firstName = fName_fld.getText().trim();
        String lastName = lName_fld.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            pAddress_lbl.setText("");
            return;
        }

        String payee = generateFormattedPayeeAddress(firstName, lastName);
        pAddress_lbl.setText(payee);
    }
    private String generateFormattedPayeeAddress(String firstName, String lastName) {
        char firstInitial = Character.toLowerCase(firstName.charAt(0));
        String basePayee = "@" + firstInitial + lastName.toLowerCase().replaceAll("\\s+", "");
        String candidate = basePayee;
        int suffix = 0;

        while (payeeAddressExists(candidate)) {
            suffix++;
            candidate = basePayee + suffix;
        }

        return candidate;
    }
    private boolean payeeAddressExists(String payee) {
        try {
            String query = "SELECT 1 FROM Clients WHERE PayeeAddress = ? LIMIT 1";
            PreparedStatement statement = databaseDriver.getConnection().prepareStatement(query);
            statement.setString(1, payee);
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();
            rs.close();
            statement.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Treat errors as 'not exists' so generation proceeds
        }
    }

    private String generateRandomCheckingAccountNumber(){
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        String formatted = String.format("%04d %04d", number / 10000, number % 10000);
        return formatted;
    }
    private String generateRandomSavingAccountNumber(){
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        String formatted = String.format("%04d %04d", number / 10000, number % 10000);
        return formatted;
    }

    private void createClient() {

        // Read inputs
        String f = fName_fld.getText();
        String l = lName_fld.getText();
        String p = pAddress_lbl.getText();
        String ca = ch_acc_lbl.getText();
        String sa = sv_acc_lbl.getText();
        String pass = password_fld.getText();

        boolean checking = ch_acc_box.isSelected();
        boolean saving = sv_acc_box.isSelected();

        double chAmount = checking ? Double.parseDouble(ch_amount_fld.getText()) : 0;
        double svAmount = saving ? Double.parseDouble(sv_amount_fld.getText()) : 0;

        // Validation
        if (f.isEmpty() || l.isEmpty() || p.isEmpty() || pass.isEmpty()) {
            error_lbl.setText("Please fill all required fields.");
            return;
        }

        if (!checking && !saving) {
            error_lbl.setText("Select at least one account type.");
            return;
        }

        boolean success = Model.getInstance().createNewClient(
                f, l, p, pass,ca,sa,checking, saving, chAmount, svAmount
        );

        if (success) {
            error_lbl.setText("Client Created Successfully!");

            // Clear fields
            fName_fld.clear();
            lName_fld.clear();
            password_fld.clear();
            pAddress_fld.setSelected(false);
            pAddress_lbl.setText("");
            ch_acc_box.setSelected(false);
            sv_acc_box.setSelected(false);
            ch_amount_fld.clear();
            sv_amount_fld.clear();
            ch_acc_lbl.setText("");
            sv_acc_lbl.setText("");
        } else {
            error_lbl.setText("Error while creating client.");
        }
    }

}
