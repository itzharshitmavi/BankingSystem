package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Views.BankAccountType;
import com.maviharshit.bankingsystem.Views.ClientCellDepositFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    @FXML
    public TextField pAddress_fld;
    @FXML
    public ListView<Client> result_listview;
    @FXML
    public Button search_btn;
    @FXML
    public TextField amount_fld;
    @FXML
    public Button deposit_btn;
    @FXML
    public Label error_lbl;
    @FXML
    public ChoiceBox<BankAccountType> deposit_acc_type;

    private ObservableList<Client> searchResults = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deposit_acc_type.setItems(FXCollections.observableArrayList(BankAccountType.CHECKING, BankAccountType.SAVING));
        result_listview.setItems(searchResults);
        result_listview.setCellFactory(param -> new ClientCellDepositFactory());
        result_listview.getSelectionModel().selectedItemProperty().addListener((obs, oldClient, newClient) -> {result_listview.refresh();});

        search_btn.setOnAction(event -> onSearch());
        deposit_btn.setOnAction(event -> onDeposit());
    }
    private void onSearch() {
        String queryAddress = pAddress_fld.getText().trim();
        error_lbl.setText("");
        searchResults.clear();

        if (queryAddress.isEmpty()) {
            error_lbl.setText("Please enter a Payee Address to search.");
            return;
        }

        try {
            ResultSet rs = Model.getInstance().getDatabaseDriver().getClientsByPayeeAddress(queryAddress);
            List<Client> tempList = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("PayeeAddress"),
                        null, null, null, rs.getString("Password")
                );
                tempList.add(client);
            }
            if (tempList.isEmpty()) {
                error_lbl.setText("No clients found with this Payee Address.");
            } else {
                searchResults.addAll(tempList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error_lbl.setText("Error during search.");
        }
    }

    private void onDeposit() {
        Client selectedClient = result_listview.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            error_lbl.setText("Please select a client from search results.");
            return;
        }

        String amountStr = amount_fld.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                error_lbl.setText("Please enter a positive amount.");
                return;
            }
        } catch (NumberFormatException e) {
            error_lbl.setText("Invalid amount entered.");
            return;
        }
        BankAccountType selectedAccountType = deposit_acc_type.getValue();
        if (selectedAccountType == null) {
            error_lbl.setText("Please select an account type.");
            return;
        }

        boolean success;
        switch (selectedAccountType) {
            case CHECKING:
                success = Model.getInstance().getDatabaseDriver()
                        .depositToClientChecking(selectedClient.pAddressProperty().get(), amount);
                break;
            case SAVING:
                success = Model.getInstance().getDatabaseDriver()
                        .depositToClientSaving(selectedClient.pAddressProperty().get(), amount);
                break;
            default:
                error_lbl.setText("Invalid account type selected.");
                return;
        }
        if (success) {
            error_lbl.setText("Deposit successful.");
            amount_fld.setText("");
            onSearch();
        } else {
            error_lbl.setText("Deposit failed. Try again.");
        }
    }

}
