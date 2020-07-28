package org.hairCareApp.alerts;

import javafx.scene.control.Alert;

// Class used for create specific notifications
public final class Alerts {
    // Error notification
    public static void showError(String errorContent) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Błąd");
        error.setHeaderText(null);
        error.setContentText(errorContent);
        error.showAndWait();
    }
    // Info notification
    public static void showInformation(String content) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Info");
        info.setContentText(content);
        info.setHeaderText(null);
        info.showAndWait();
    }

}
