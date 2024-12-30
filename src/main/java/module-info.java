module com.operating {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.operating to javafx.fxml;
    exports com.operating;
    exports com.operating.controller;
    exports com.operating._process_manage;
    opens com.operating.controller to javafx.fxml;
    exports com.operating.model;
}