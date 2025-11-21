package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Models.Transaction;
import com.maviharshit.bankingsystem.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {
    @FXML
    public ListView<Transaction> transactions_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = Model.getInstance().getClient();

        List<Transaction> transactions = Model.getInstance().getTransactionHistoryForClient(client);
        transactions_listview.setItems(FXCollections.observableArrayList(transactions));
        transactions_listview.setCellFactory(param -> new TransactionCellFactory());
    }
}
