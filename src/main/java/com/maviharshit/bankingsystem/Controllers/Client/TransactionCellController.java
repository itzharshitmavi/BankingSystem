package com.maviharshit.bankingsystem.Controllers.Client;

import com.maviharshit.bankingsystem.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {
    @FXML
    public FontAwesomeIconView in_icon;
    @FXML
    public FontAwesomeIconView out_icon;
    @FXML
    public Label transaction_date_lbl;
    @FXML
    public Label sender_lbl;
    @FXML
    public Label receiver_lbl;
    @FXML
    public Label amount_lbl;
    private final Transaction transaction;

    public TransactionCellController(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaction_date_lbl.setText(transaction.dateProperty().get().toString());
        sender_lbl.setText(transaction.senderProperty().get());
        receiver_lbl.setText(transaction.receiverProperty().get());
        amount_lbl.setText(String.format("₹%.2f", transaction.amountProperty().get()));
        // incoming if current user is receiver
        String me = com.maviharshit.bankingsystem.Models.Model.getInstance().getClient().pAddressProperty().get();
        if(me != null && me.equals(transaction.receiverProperty().get())){
            in_icon.setVisible(true); out_icon.setVisible(false);
        } else {
            in_icon.setVisible(false); out_icon.setVisible(true);
        }
        String msg = transaction.messageProperty().get();
        if(msg != null && !msg.isBlank()){
            Tooltip t = new Tooltip(msg);
            Tooltip.install(amount_lbl, t);
        }
    }
}
