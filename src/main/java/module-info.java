module com.example.flightintheatmospherelab1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.flightintheatmospherelab1 to javafx.fxml;
    exports com.example.flightintheatmospherelab1;
}