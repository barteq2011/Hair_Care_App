package org.hairCareApp.dataOperating;

import org.hairCareApp.Product;
import org.hairCareApp.alerts.Alerts;
import org.hairCareApp.dataObjects.SavedDate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class DateBank {
    // This file must exist in the system, instalator of app should unpack it to the appropiate folder
    private final String fileName = System.getProperty("user.home")+"/Documents/HairCareAppDatabaseFiles/dateData.csv";
    // List to store Dates
    private final List<SavedDate> savedDates;

    private DateBank() { savedDates = new ArrayList<>(); }
    public static DateBank newDateBank() { return new DateBank(); }
    // File operations
    public void loadDatesDatabaseFile(ProductBank productData) {
        Path filePath = Paths.get(fileName);
        // Create reader for database file
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String dataRow;
            while ((dataRow = reader.readLine()) != null) {
                // Splitting data because of .csv format
                String[] dataCell = dataRow.split(";");
                if(dataCell.length>1) {
                    String date = dataCell[0];
                    SavedDate dateToLoad = SavedDate.newSavedDate(date);
                    for (int i = 1; i < dataCell.length; i++) {
                        String productName = dataCell[i];
                        Product product = productData.getProduct(productName);
                        if (product != null)
                            dateToLoad.addProduct(product);
                    }
                    savedDates.add(dateToLoad);
                }
            }
        } catch (IOException e) {
            Alerts.showError("Error reading from database file, check if every file exists, if not, reinstall program");
        }
    }
    public void saveDatesToDatabaseFile() {
        Path filePath = Paths.get(fileName);
        // Creat writer for db file
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (SavedDate date : savedDates) {
                StringBuilder productsFormat = new StringBuilder();
                // Add products names to date record
                for (Product product : date.getProducts()) {
                    productsFormat.append(";").append(product.getName());
                }
                String finalFormat;
                // Check if there are products
                if (productsFormat.length() > 0)
                    finalFormat = date.getDate() + productsFormat;
                else
                    finalFormat = date.getDate() + ";";
                // Write to file
                writer.write(finalFormat);
                writer.newLine();
            }
        } catch (IOException e) {
            Alerts.showError("Error saving database file, check if every file exists, if not, reinstall program");
        }
    }
    // Dates Bank operations
    // Get date object from savedDates List
    public SavedDate getDate(String date) {
        SavedDate finalDate = null;
        for( SavedDate iterDate : savedDates )
            if(iterDate.getDate().equals(date)) {
                // Get specific date from database
                finalDate = iterDate;
                break;
            }
        return finalDate;
    }
    public void addDate(SavedDate date) {
        // Date must not exist in database
        if(getDate(date.getDate()) == null)
            savedDates.add(date);
    }
}