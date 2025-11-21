package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Views.ClientMenuOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    @FXML
    public Button accounts_btn;

    @FXML
    public Button dashboard_btn;

    @FXML
    public Button logout_btn;

    @FXML
    public Button profile_btn;

    @FXML
    public Button report_btn;

    @FXML
    public Button transactions_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    private void addListener(){
        dashboard_btn.setOnAction(event -> onDashboard());
        transactions_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        profile_btn.setOnAction(event ->onProfile());
    }
    private void onDashboard(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }
    private void onTransactions(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }
    private void onAccounts(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }
    private void onProfile(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Login.fxml"));

            // Set login page on the same window
            stage.setScene(new Scene(root));
            stage.show();
            Model.getInstance().setClientLoginSuccessFlag(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
