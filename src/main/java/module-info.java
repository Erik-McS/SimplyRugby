module com.application.simplyrugby {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.xerial.sqlitejdbc;
    requires org.testng;


    opens com.application.simplyrugby to javafx.fxml;
    exports com.application.simplyrugby;
    exports com.application.simplyrugby.Model;
    opens com.application.simplyrugby.Model to javafx.fxml;
    exports com.application.simplyrugby.System;
    opens com.application.simplyrugby.System to javafx.fxml;
    exports com.application.simplyrugby.Control;
    opens com.application.simplyrugby.Control to javafx.fxml;
    exports com.application.simplyrugby.Testing;
    opens com.application.simplyrugby.Testing to javafx.fxml;
}
