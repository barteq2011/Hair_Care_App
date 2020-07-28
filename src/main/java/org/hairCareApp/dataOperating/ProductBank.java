package org.hairCareApp.dataOperating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hairCareApp.Product;
import org.hairCareApp.alerts.Alerts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ProductBank {
    // This file must exist in system, installer will unpack it to appropiate folder
    private static final String fileName = System.getProperty("user.home")+"/Documents/HairCareAppDatabaseFiles/productData.csv";
    // List to store products got from database file
    private ObservableList<Product> products;

    private ProductBank() { products = FXCollections.observableArrayList(); }
    public static ProductBank newProductBank() { return new ProductBank(); }
    // File operations
    public void loadProductsDatabaseFile() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        Path filePath = Paths.get(fileName);
        // Create reader for database file
        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String dataRow;
            while ((dataRow = reader.readLine()) != null) {
                // Splitting data because of .csv format
                String[] dataCell = dataRow.split(";");
                if (dataCell.length > 1) {
                    String name = dataCell[0];
                    String type = dataCell[1];
                    String peh = dataCell[2];
                    String purchaseDate = dataCell[3];
                    Product product = Product.newProduct(name, type, peh, purchaseDate);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            Alerts.showError("Error reading database file, check if every file exists, if not, reinstall program");
        }
        // Overwite list of products
        this.products = products;
    }
    public void saveProdutsToDatabaseFile() {
        Path filePath = Paths.get(fileName);
        // Create writer for database file
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Product product : products) {
                // Save products in appropiate format
                writer.write(String.format("%s;%s;%s;%s", product.getName(), product.getType(), product.getPeh(), product.getPurchaseDate()));
                writer.newLine();
            }
        }catch (IOException e) {
            Alerts.showError("Error saving database file, check if every file exists, if not, reinstall program");
        }
    }
    // Products Bank operations
    public ObservableList<Product> getProducts() { return products; }
    // Get specific product from database
    public Product getProduct(String productName) {
        Product foundProduct = null;
        for(Product product : products)
            if(product.getName().equals(productName)) {
                foundProduct = product;
                break;
            }
        return foundProduct;
    }
    public void addProduct(Product product) { products.add(product); }
    public void deleteProduct(Product product) {
        // Product must exist in database
        if(getProduct(product.getName()) != null)
            products.remove(product);
    }
}
