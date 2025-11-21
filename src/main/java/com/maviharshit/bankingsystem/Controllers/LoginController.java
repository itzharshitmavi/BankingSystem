package com.maviharshit.bankingsystem.Controllers;

import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public ChoiceBox<AccountType> acc_selector;

    @FXML
    public Label error_lbl;

    @FXML
    public Button login_btn;

    @FXML
    public PasswordField password_field;

    @FXML
    public TextField payee_address_fld;

    @FXML
    public Label payee_address_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.ADMIN, AccountType.CLIENT));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue()));
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_field.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                payee_address_fld.setText("");
                password_field.setText("");
                error_lbl.setText("Invalid Client Credentials!");
            }
        } else {
            Model.getInstance().evaluateAdminCred(
                    payee_address_fld.getText(),
                    password_field.getText()
            );
            if (Model.getInstance().getAdminLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showAdminWindow();
                stage = (Stage) error_lbl.getScene().getWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                error_lbl.setText("Invalid Admin Credentials!");
                payee_address_fld.clear();
                password_field.clear();
            }
        }
    }
}