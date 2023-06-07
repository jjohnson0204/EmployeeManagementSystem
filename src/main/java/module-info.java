module com.example.employeemanagementsystem {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.employeemanagementsystem to javafx.fxml;
    exports com.example.employeemanagementsystem;
}