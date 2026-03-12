module com.example.escrirapmp1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.escrirapmp1 to javafx.fxml;
    opens com.example.escrirapmp1.controller to javafx.fxml;

    exports com.example.escrirapmp1;
}
