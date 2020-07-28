package org.hairCareApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hairCareApp.dataOperating.DateBank;
import org.hairCareApp.dataOperating.ProductBank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public final class App extends Application {
    private double XOffset, YOffset;
    LocalDate actualDate;
    DateBank dateBank;
    ProductBank productBank;
    DateTimeFormatter formatter;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initial data loading for initial modify of program window
        formatter = DateTimeFormatter.ofPattern("d MMM uuuu");
        actualDate = LocalDate.now();
        dateBank = DateBank.newDateBank();
        productBank = ProductBank.newProductBank();
        productBank.loadProductsDatabaseFile();
        dateBank.loadDatesDatabaseFile(productBank);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainWindow.fxml"));
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("styles/icon.png"))));
        Parent root = loader.load();
        Scene scene = new Scene(root, 300, 230);
        int finalHeight = 230;
        if(dateBank.getDate(actualDate.format(formatter)) != null) {
            int size = dateBank.getDate(actualDate.format(formatter)).getProducts().size();
            if(size>3)
                for(int i=0;i<size-1;i++)
                    finalHeight+=40;
        }
        primaryStage.setHeight(finalHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hair Care App");
        primaryStage.setOpacity(0.92);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        MainWindowController controller = loader.getController();
        // Adding handlers to menuBar to make window draggable
        controller.getMenuBar().setOnMousePressed(e -> {
            XOffset = e.getSceneX();
            YOffset = e.getSceneY();
        });
        controller.getMenuBar().setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX() - XOffset);
            primaryStage.setY(e.getScreenY() - YOffset);
        });

        primaryStage.setOnCloseRequest(e -> {
            try {
                controller.getSavedDatesBank().saveDatesToDatabaseFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}