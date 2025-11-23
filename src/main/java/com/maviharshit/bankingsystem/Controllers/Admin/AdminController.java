package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, olVal, newVal) -> {
            switch (newVal){
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case DEPOSIT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositView());
                case REPORT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCheckView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClient());
            }
        });
    }
}
