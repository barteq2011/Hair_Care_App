module org.mainPackage {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.hairCareApp to javafx.fxml;
    exports org.hairCareApp;
}