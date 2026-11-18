module org.example.gamble {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gamble to javafx.fxml;
    exports org.example.gamble;
}
