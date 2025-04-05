module CSC3402.Database.Application.Development {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens LAB2 to javafx.fxml;
    exports LAB2;
}