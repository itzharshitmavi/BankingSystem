module com.maviharshit.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires javafx.base;

    opens com.maviharshit.bankingsystem to javafx.fxml;
    opens com.maviharshit.bankingsystem.Controllers to javafx.fxml;
    opens com.maviharshit.bankingsystem.Controllers.Admin to javafx.fxml;
    opens com.maviharshit.bankingsystem.Controllers.Client to javafx.fxml;
    opens com.maviharshit.bankingsystem.Models to javafx.fxml;
    exports com.maviharshit.bankingsystem;
    exports com.maviharshit.bankingsystem.Controllers;
    exports com.maviharshit.bankingsystem.Controllers.Admin;
    exports com.maviharshit.bankingsystem.Controllers.Client;
    exports com.maviharshit.bankingsystem.Models;
    exports com.maviharshit.bankingsystem.Views;
}