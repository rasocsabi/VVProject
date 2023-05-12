module com.example.signup {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;
    requires org.mockito;
    requires org.junit.jupiter.api;
    requires org.testfx.junit5;


    opens com.example.signup to javafx.fxml;
    exports com.example.signup;
}