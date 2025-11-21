package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Client;
import com.maviharshit.bankingsystem.Models.Model;
import com.maviharshit.bankingsystem.Models.Transaction;
import com.maviharshit.bankingsystem.Views.BankAccountType;
import com.maviharshit.bankingsystem.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    public TextField amount_fld;

    @FXML
    public Label checking_acc_nam;

    @FXML
    public Label checking_bal;

    @FXML
    public Label expense_lbl;

    @FXML
    public Label income_lbl;

    @FXML
    public Label login_date;

    @FXML
    public TextField message_fld;

    @FXML
    public TextField payee_fld;

    @FXML
    public Label saving_bal;

    @FXML
    public Button send_money_btn;

    @FXML
    public ListView<Transaction> transaction_listview;

    @FXML
    public Text user_name;
    @FXML
    public Label transaction_date_lbl;
    @FXML
    public Label sender_lbl;
    @FXML
    public Label amount_lbl;
    @FXML
    public Label receiver_lbl;
    @FXML
    public Label Saving_acc_nam;
    @FXML
    public Label error_lbl;
    @FXML
    public ChoiceBox<BankAccountType> send_money_acc_type;
    @FXML
    public TextField acc_num_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        send_money_acc_type.setItems(FXCollections.observableArrayList(BankAccountType.CHECKING, BankAccountType.SAVING));
        Client client = Model.getInstance().getClient();

        user_name.setText("Hello, " + client.firstNameProperty().get());
        login_date.setText("Today, " + LocalDate.now().toString());

        if (client.checkingAccountProperty().get() != null) {
            checking_bal.setText(String.format("%.2f", client.checkingAccountProperty().get().getBalance()));
            checking_acc_nam.setText(client.checkingAccountProperty().get().getAccountNumber());
        } else {
            checking_bal.setText("N/A");
            checking_acc_nam.setText("N/A");
        }
        if (client.savingAccountProperty().get() != null) {
            saving_bal.setText(String.format("%.2f", client.savingAccountProperty().get().getBalance()));
            Saving_acc_nam.setText(client.savingAccountProperty().get().getAccountNumber());
        } else {
            saving_bal.setText("N/A");
            Saving_acc_nam.setText("N/A");
        }

        // Load latest transactions
        List<Transaction> transactions = Model.getInstance().getTransactionHistoryForClient(client);
        transaction_listview.setItems(FXCollections.observableArrayList(transactions));
        transaction_listview.setCellFactory(param -> new TransactionCellFactory());

        // Calculate income (sum of received amounts) and expenses (sent amounts)
        double incomeTotal = transactions.stream()
                .filter(t -> t.receiverProperty().get().equals(client.pAddressProperty().get()))
                .mapToDouble(t -> t.amountProperty().get())
                .sum();

        double expenseTotal = transactions.stream()
                .filter(t -> t.senderProperty().get().equals(client.pAddressProperty().get()))
                .mapToDouble(t -> t.amountProperty().get())
                .sum();

        income_lbl.setText("+ ₹" + String.format("%.2f", incomeTotal));
        expense_lbl.setText("- ₹" + String.format("%.2f", expenseTotal));

        // Set Send Money button action
        send_money_btn.setOnAction(event -> onSendMoney());
    }

    private void onSendMoney() {
        String receiverPayee = payee_fld.getText().trim();
        String receiverAccNum = acc_num_fld.getText().trim();
        String amountStr = amount_fld.getText().trim();
        String message = message_fld.getText().trim();

        if (receiverPayee.isEmpty() || receiverAccNum.isEmpty() || amountStr.isEmpty()) {
            error_lbl.setText("Please fill all fields.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                error_lbl.setText("Enter a valid positive amount.");
                return;
            }
        } catch (NumberFormatException e) {
            error_lbl.setText("Invalid amount format.");
            return;
        }

        BankAccountType senderAccountType = send_money_acc_type.getValue();

        boolean success = Model.getInstance().sendMoneyToAccount(
                receiverPayee,
                receiverAccNum,
                amount,
                message,
                senderAccountType
        );

        if (success) {
            error_lbl.setText("Money sent successfully.");
            Model.getInstance().loadClientAccounts(Model.getInstance().getClient().getPayeeAddress());
            updateBalancesOnUI();
            refreshTransactions();
            clearSendMoneyFields();
        } else {
            error_lbl.setText("Transfer failed. Check details and balances.");
        }
    }

    private void clearSendMoneyFields() {
        payee_fld.clear();
        acc_num_fld.clear();
        amount_fld.clear();
        message_fld.clear();
        send_money_acc_type.setValue(BankAccountType.CHECKING);
    }

    private void updateBalancesOnUI() {
        Client client = Model.getInstance().getClient();

        if (client.getCheckingAccount() != null) {
            checking_bal.setText(String.format("%.2f", client.getCheckingAccount().getBalance()));
            checking_acc_nam.setText(client.getCheckingAccount().getAccountNumber());
        } else {
            checking_bal.setText("N/A");
            checking_acc_nam.setText("N/A");
        }

        if (client.getSavingAccount() != null) {
            saving_bal.setText(String.format("%.2f", client.getSavingAccount().getBalance()));
            Saving_acc_nam.setText(client.getSavingAccount().getAccountNumber());
        } else {
            saving_bal.setText("N/A");
            Saving_acc_nam.setText("N/A");
        }
    }

    private void refreshTransactions() {
        Client client = Model.getInstance().getClient();
        List<Transaction> transactions = Model.getInstance().getTransactionHistoryForClient(client);
        transaction_listview.setItems(FXCollections.observableArrayList(transactions));
    }
}
