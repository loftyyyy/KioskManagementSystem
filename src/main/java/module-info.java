module org.example.softfun_funsoft {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    requires com.dlsc.formsfx;
    requires org.controlsfx.controls;
    requires javafx.media;
    requires dotenv.java;
    requires stripe.java;
    requires org.json;
    requires java.desktop;
    requires javafx.web;
    requires java.sql;
    requires com.zaxxer.hikari;


    opens org.example.softfun_funsoft to javafx.fxml;
    exports org.example.softfun_funsoft;
    exports org.example.softfun_funsoft.listener;
    opens org.example.softfun_funsoft.listener to javafx.fxml;
}