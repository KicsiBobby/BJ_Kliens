module com.example.bj {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bj to javafx.fxml;
    exports com.example.bj;
}