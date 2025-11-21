package com.maviharshit.bankingsystem.Views;

import com.maviharshit.bankingsystem.Controllers.Client.TransactionCellController;
import com.maviharshit.bankingsystem.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TransactionCellFactory extends ListCell<Transaction> {
    private final HBox content;
    private final Text sender;
    private final Text receiver;
    private final Text amount;
    private final Text date;
    private final Text message;

    public TransactionCellFactory() {
        super();
        sender = new Text();
        receiver = new Text();
        amount = new Text();
        date = new Text();
        message = new Text();
        content = new HBox(10, sender, receiver, amount, date, message);
    }
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/TransactionCell.fxml"));
            TransactionCellController controller = new TransactionCellController(transaction);
            loader.setController(controller);
            setText(null);
            try{
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
