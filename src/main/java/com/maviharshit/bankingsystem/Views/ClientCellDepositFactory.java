package com.maviharshit.bankingsystem.Views;

import com.maviharshit.bankingsystem.Controllers.Admin.ClientCellDepositController;
import com.maviharshit.bankingsystem.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class ClientCellDepositFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCellDeposit.fxml"));
            ClientCellDepositController controller = new ClientCellDepositController(client);
            loader.setController(controller);
            setText(null);
            try {
                AnchorPane pane = loader.load();
                setGraphic(pane);

                // Check if this client is selected in ListView and color button
                boolean isSelected = getListView().getSelectionModel().getSelectedItem() == client;
                if (isSelected) {
                    controller.getSelect_btn().setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                } else {
                    controller.getSelect_btn().setStyle(""); // reset style
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
