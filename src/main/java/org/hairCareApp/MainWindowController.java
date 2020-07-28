package org.hairCareApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hairCareApp.alerts.Alerts;
import org.hairCareApp.dataObjects.SavedDate;
import org.hairCareApp.dataOperating.DateBank;
import org.hairCareApp.dataOperating.ProductBank;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class MainWindowController {
    private double dialogXOffset, dialogYOffset;
    private LocalDate actualDate;
    private DateTimeFormatter formatter;
    private DateBank savedDatesBank;
    private ProductBank productBank;
    @FXML private Button buttonLeft, buttonRight;
    @FXML private MenuBar menuBar;
    @FXML private BorderPane mainBorderPane;
    @FXML private Label dateLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane dateProductsGridPane;

    @FXML public void initialize() {
        // Apperance values
        buttonLeft.setText("<");
        buttonRight.setText(">");
        datePicker.getEditor().setVisible(false);
        // Initial operations
        formatter = DateTimeFormatter.ofPattern("d MMM uuuu");
        productBank = ProductBank.newProductBank();
        savedDatesBank = DateBank.newDateBank();
        productBank.loadProductsDatabaseFile();
        savedDatesBank.loadDatesDatabaseFile(productBank);
        setNewActualDate(LocalDate.now(), true);
    }
    // FXML functions
    @FXML public void handleLeftButton() { setNewActualDate(actualDate.minusDays(1)); }
    @FXML public void handleRightButton() { setNewActualDate(actualDate.plusDays(1)); }
    @FXML public void handleDatePicker() {
        LocalDate chosenDate = datePicker.getValue();
        if(chosenDate!=null)
            setNewActualDate(chosenDate);
    }
    @FXML public void handleAddButton() { addProductLabelToCenterGridPane(); }
    // Create and show dialog window which allows user to create and delete products in database
    @FXML public void handleProductsMenuItem() {
        Stage dialogStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/listOfProductsDialog.fxml"));
        try {
            Parent dialogRoot = loader.load();
            dialogStage.getIcons().add(new Image(String.valueOf(getClass().getResource("styles/icon.png"))));
            dialogStage.setTitle("Twoje produkty");
            ListOfProductsDialogController controller = loader.getController();
            Scene dialogScene = new Scene(dialogRoot, 500, 300);
            dialogStage.setScene(dialogScene);
            dialogStage.initOwner(mainBorderPane.getScene().getWindow());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogScene.setFill(Color.TRANSPARENT);
            // Add handlers to menubar to make window draggable
            controller.getMainMenuBar().setOnMousePressed(e -> {
                dialogXOffset = e.getSceneX();
                dialogYOffset = e.getSceneY();
            });
            controller.getMainMenuBar().setOnMouseDragged(e -> {
                dialogStage.setX(e.getScreenX() - dialogXOffset);
                dialogStage.setY(e.getScreenY() - dialogYOffset);
            });
            dialogStage.showAndWait();
            // Reload list of products in database
            productBank.loadProductsDatabaseFile();
            // Reload dates with products assigned to them
            savedDatesBank.loadDatesDatabaseFile(productBank);
            setNewActualDate(actualDate);
        } catch (IOException e) {
            Alerts.showError("Can't open dialog window, check if appropiate files exist");
        }
    }
    @FXML public void hanleAboutMenuItem() {
        Alerts.showInformation("Program wykonany przez kochającego chłopaka dla swojej niezorganizowanej, " +
                "uroczej dziewczyny <3");
    }
    @FXML public void handleExitMenuItem() {
        // Saved dates before closing program
        savedDatesBank.saveDatesToDatabaseFile();
        Platform.exit();
    }
    // No FXML functions
    private void setNewActualDate(LocalDate date, boolean isInitial) {
        // Modoify window size to store products assigned to actual date
        if(!isInitial) mainBorderPane.getScene().getWindow().setHeight(230);
        this.actualDate = date;
        SavedDate actualDate = SavedDate.newSavedDate(date.format(formatter));
        dateLabel.setText(actualDate.getDate());
        if(!isInitial)
            fillCenterFields(actualDate);
        else
            fillCenterFields(actualDate, true);
    }
    private void setNewActualDate(LocalDate date) {
        setNewActualDate(date, false);
    }
    // Menubar access for making window draggable
    public MenuBar getMenuBar() { return menuBar; }
    public DateBank getSavedDatesBank() { return savedDatesBank; }
    // Fill center of window with products assigned to acutal date
    private void fillCenterFields(SavedDate date, boolean isInitial) {
        dateProductsGridPane.getChildren().clear();
        if(savedDatesBank.getDate(date.getDate()) != null) {
            date = savedDatesBank.getDate(date.getDate());
            for(Product product : date.getProducts())
                if(!isInitial)
                    addProductLabelToCenterGridPane(product);
                else
                    addProductLabelToCenterGridPane(product, true);
        }
    }
    private void fillCenterFields(SavedDate date) { fillCenterFields(date, false); }
    // Generate products fields in center grid pane, which allows to assign new product to date,
    // unassign it, and view products assigned to date
    private void addProductLabelToCenterGridPane(Product product, boolean isInitial) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(true);
        checkBox.setDisable(true);
        int row = dateProductsGridPane.getRowCount()+1;
        dateProductsGridPane.add(checkBox, 0, row);
        ComboBox<Product> comboBox = new ComboBox<>();
        comboBox.setItems(productBank.getProducts());
        dateProductsGridPane.add(comboBox, 1, row);
        Button deleteButton = new Button("Usuń");
        dateProductsGridPane.add(deleteButton, 2, row);
        deleteButton.setOnAction(e -> {
            SavedDate actualDate = savedDatesBank.getDate(this.actualDate.format(formatter));
            if(actualDate != null)
                actualDate.removeProduct(comboBox.getValue());
            dateProductsGridPane.getChildren().removeAll(comboBox, checkBox, deleteButton);
            if(row>3) getStage().setHeight(getStage().getHeight()-40);
        });
        if(!isInitial) {
            if (row > 3) getStage().setHeight(getStage().getHeight() + 40);
        }
        SavedDate actualDate = savedDatesBank.getDate(this.actualDate.format(formatter));
        if(actualDate == null) {
            actualDate = SavedDate.newSavedDate(this.actualDate.format(formatter));
            savedDatesBank.addDate(actualDate);
        }
        SavedDate finalActualDate = actualDate;
        if(product != null) {
            comboBox.getSelectionModel().select(product);
            Product mainSelectedProduct = comboBox.getSelectionModel().getSelectedItem();
            comboBox.setOnAction(e -> {
                Product selectedProduct = comboBox.getValue();
                finalActualDate.removeProduct(mainSelectedProduct);
                finalActualDate.addProduct(selectedProduct);
            });
        } else {
            comboBox.setOnAction(e -> {
                Product selectedProduct = comboBox.getValue();
                finalActualDate.addProduct(selectedProduct);
            });
        }
    }
    private void addProductLabelToCenterGridPane(Product product) { addProductLabelToCenterGridPane(product, false); }
    private Stage getStage() { return (Stage) mainBorderPane.getScene().getWindow(); }
    private void addProductLabelToCenterGridPane() {
        addProductLabelToCenterGridPane(null);
    }
}
