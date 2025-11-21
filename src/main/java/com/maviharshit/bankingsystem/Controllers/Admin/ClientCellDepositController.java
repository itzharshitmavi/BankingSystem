package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellDepositController implements Initializable {
    @FXML
    private Label ch_acc_lbl;

    @FXML
    private Label date_lbl;

    @FXML
    private Button select_btn;

    @FXML
    private Label fName_lbl;

    @FXML
    private Label lName_lbl;

    @FXML
    private Label pAddress_lbl;

    @FXML
    private Label sv_acc_lbl;
    private final Client client;
    private ClientsController clientsController;

    public Button getSelect_btn() {
        return select_btn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            fName_lbl.setText(client.firstNameProperty().get());
            lName_lbl.setText(client.lastNameProperty().get());
            pAddress_lbl.setText(client.pAddressProperty().get());
    }

    public ClientCellDepositController(Client client) {
        this.client = client;
    }
    public void setClientsController(ClientsController clientsController) {
        this.clientsController = clientsController;
    }

}
