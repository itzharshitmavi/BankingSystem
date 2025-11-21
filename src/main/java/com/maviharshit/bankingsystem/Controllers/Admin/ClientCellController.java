package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    @FXML
    public Label fName_lbl;
    @FXML
    public Label pAddress_lbl;
    @FXML
    public Label ch_acc_lbl;
    @FXML
    public Label lName_lbl;
    @FXML
    public Label sv_acc_lbl;
    @FXML
    public Label date_lbl;
    @FXML
    public Button delete_btn;
    @FXML
    public Label pass_lbl;
    private final Client client;
    private ClientsController clientsController;

    public ClientCellController(Client client) {
        this.client = client;
    }
    public void setClientsController(ClientsController clientsController) {
        this.clientsController = clientsController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_lbl.setText(client.firstNameProperty().get());
        lName_lbl.setText(client.lastNameProperty().get());
        pAddress_lbl.setText(client.pAddressProperty().get());
        pass_lbl.setText(client.passwordProperty().get());
        delete_btn.setOnAction(event -> {
            boolean deleted = Model.getInstance().getDatabaseDriver().deleteClient(client.pAddressProperty().get());
            if (deleted && clientsController != null) {
                clientsController.refreshClients(); // Refresh the client list on parent controller
            }
        });
    }
}
