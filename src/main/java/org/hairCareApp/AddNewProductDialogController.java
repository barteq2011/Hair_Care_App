package org.hairCareApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public final class AddNewProductDialogController {
    DateTimeFormatter formatter;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<String> typeComboBox, pehTypeComboBox;
    @FXML private DatePicker purDatePicker;

    @FXML public void initialize() {
        // Formatter used for purchase date
        formatter = DateTimeFormatter.ofPattern("d MMM uuuu");
        // User can't enter date manually
        purDatePicker.getEditor().setDisable(true);
        // Strings used for type of product choosing
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Szampon");
        types.add("Odżywka");
        types.add("Maska");
        types.add("Serum");
        types.add("Balsam");
        types.add("Olej");
        types.add("Olejek");
        types.add("Zioła");
        types.add("Wcierka");
        types.add("Peeling");
        types.add("DIY");
        types.add("Inne");
        // Fill combobox with strings
        typeComboBox.setItems(types);
        // Strings used for peh type of product choosing
        ObservableList<String> pehTypes = FXCollections.observableArrayList();
        pehTypes.add("N.D");
        pehTypes.add("Proteiny");
        pehTypes.add("Emolienty");
        pehTypes.add("Humektanty");
        pehTypes.add("PEH");
        pehTypes.add("Humektanty + P/E");
        pehTypes.add("Emolienty + P/H");
        // Fill combobox with strings
        pehTypeComboBox.setItems(pehTypes);
    }
    // Create products specified by user
    public Product getProductFromFields() {
        String name = nameTextField.getText();
        boolean errorFlag = false;
        String type = "";
        String peh = "";
        String purchaseDate = "";
        if(typeComboBox.getValue() != null)
            type = typeComboBox.getValue();
        else
            errorFlag = true;
        if(pehTypeComboBox.getValue() != null)
            peh = pehTypeComboBox.getValue();
        else
            errorFlag = true;
        if(purDatePicker.getValue()!=null)
            purchaseDate = purDatePicker.getValue().format(formatter);
        else
            errorFlag = true;
        // Name can't contain any of key characters used for storing product in database
        if(!name.contains(";") && !name.isEmpty() && !errorFlag)
            return Product.newProduct(name, type, peh, purchaseDate);
        return null;
    }
}
