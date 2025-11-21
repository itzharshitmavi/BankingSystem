package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML
    public ListView<Client> clients_listview;
    private final ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadClientsFromDB();
        clients_listview.setItems(clientsList);
        clients_listview.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
                        ClientCellController controller = new ClientCellController(client);
                        loader.setController(controller);
                        Parent cellRoot = loader.load();

                        // Inject this ClientsController reference into cell controller for callbacks
                        controller.setClientsController(ClientsController.this);

                        setText(null);
                        setGraphic(cellRoot);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setText("Error loading client cell."); // fallback text
                        setGraphic(null);
                    }
                }
            }
        });    }
    private void loadClientsFromDB() {
        clientsList.clear();
        try {
            ResultSet rs = Model.getInstance().getDatabaseDriver().getAllClients();  // Create this method in DatabaseDriver
            List<Client> tempList = new ArrayList<>();
            while(rs.next()) {
                // Extract client data from result set
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String payeeAddress = rs.getString("PayeeAddress");
                String password = rs.getString("Password");
                // For accounts and dateCreated you can similarly extract and create Account objects if needed
                Client client = new Client(firstName, lastName, payeeAddress, null, null, null, password);
                tempList.add(client);
            }
            clientsList.addAll(tempList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Call this method to refresh clients list after deletion
    public void refreshClients() {
        loadClientsFromDB();
    }
}
