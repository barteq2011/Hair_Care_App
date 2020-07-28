package org.hairCareApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.hairCareApp.alerts.Alerts;
import org.hairCareApp.dataOperating.ProductBank;
import java.util.Optional;


public class ListOfProductsDialogController {
    ProductBank productBank;
    @FXML private MenuBar mainMenuBar;
    @FXML private BorderPane mainBorderPane;
    @FXML private TableView<Product> productsTableView;

    @FXML public void initialize() {
        productBank = ProductBank.newProductBank();
        try {
            productBank.loadProductsDatabaseFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        productsTableView.setItems(productBank.getProducts());
        TableColumn<Product, String> nameCol = new TableColumn<>("Nazwa");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Product, String> pehCol = new TableColumn<>("PEH");
        pehCol.setCellValueFactory(new PropertyValueFactory<>("peh"));
        TableColumn<Product, String> purDateCol = new TableColumn<>("Data zakupu");
        purDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        productsTableView.getColumns().addAll(nameCol, typeCol, pehCol, purDateCol);
        productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productsTableView.getSelectionModel().selectFirst();
    }

    @FXML public void handleNewMenuItem() {
        Dialog<ButtonType> newDialog = new Dialog<>();
        newDialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        newDialog.setTitle("Dodawanie produktu");
        loader.setLocation(getClass().getResource("dialogs/addNewProductDialog.fxml"));
        try {
            newDialog.getDialogPane().setContent(loader.load());
            newDialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
            newDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> result = newDialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.APPLY) {
                AddNewProductDialogController controller = loader.getController();
                Product product = controller.getProductFromFields();
                if(product != null) {
                    productBank.addProduct(product);
                } else {
                    Alerts.showError("Wymagane są wszystkie pola\nUpewnij się że nazwa nie posiada średnika (';')");
                    handleNewMenuItem();
                }
            }
            productBank.saveProdutsToDatabaseFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void handleDeleteMenuItem() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Usuwanie produktu");
        deleteAlert.setHeaderText(null);
        deleteAlert.setContentText("Czy na pewno chcesz usunąć produkt?");
        Optional<ButtonType> result = deleteAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            productBank.deleteProduct(getSelectedProduct());
        }
        productBank.saveProdutsToDatabaseFile();
    }
    @FXML public void handleCloseMenuItem() {
        mainBorderPane.getScene().getWindow().hide();
    }
    private Product getSelectedProduct() { return productsTableView.getSelectionModel().getSelectedItem(); }
    public MenuBar getMainMenuBar() { return mainMenuBar; }
}
