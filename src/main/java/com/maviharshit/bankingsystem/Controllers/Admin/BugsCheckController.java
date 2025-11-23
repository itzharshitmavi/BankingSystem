package com.maviharshit.bankingsystem.Controllers.Admin;

import com.maviharshit.bankingsystem.Models.BugReport;
import com.maviharshit.bankingsystem.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BugsCheckController implements Initializable {

    @FXML private TableView<BugReport> bugReports_table;

    @FXML private TableColumn<BugReport, Integer> id_col;
    @FXML private TableColumn<BugReport, String> payee_col;
    @FXML private TableColumn<BugReport, String> desc_col;
    @FXML private TableColumn<BugReport, String> date_col;
    @FXML private TableColumn<BugReport, String> status_col;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup table column bindings
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        payee_col.setCellValueFactory(new PropertyValueFactory<>("payeeAddress"));
        desc_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBugReports();
    }

    private void loadBugReports() {
        ObservableList<BugReport> data = FXCollections.observableArrayList();
        ResultSet rs = Model.getInstance().getDatabaseDriver().getAllBugReports();
        try {
            while (rs.next()) {
                BugReport br = new BugReport(
                        rs.getInt("id"),
                        rs.getString("payee_address"),
                        rs.getString("description"),
                        rs.getString("report_date"),
                        rs.getString("status")
                );
                data.add(br);
            }
            bugReports_table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
