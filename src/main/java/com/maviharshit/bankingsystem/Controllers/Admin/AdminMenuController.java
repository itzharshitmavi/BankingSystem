package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Views.AdminMenuOptions;
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

public class AdminMenuController implements Initializable {

    @FXML
    public Button clients_btn;

    @FXML
    public Button create_client_btn;

    @FXML
    public Button deposit_btn;

    @FXML
    public Button logout_btn;
    @FXML
    public Button report_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    private void addListener(){
        create_client_btn.setOnAction(event -> onCreateClient());
        clients_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        report_btn.setOnAction(event -> onCheckReport());
    }
    private void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }
    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }
    private void onDeposit(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }
    private void onCheckReport(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.REPORT);
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
            Model.getInstance().setAdminLoginSuccessFlag(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
