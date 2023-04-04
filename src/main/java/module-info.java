module com.application.simplyrugby {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.application.simplyrugby to javafx.fxml;
    exports com.application.simplyrugby;
    exports com.application.simplyrugby.Control;
    opens com.application.simplyrugby.Control to javafx.fxml;
}