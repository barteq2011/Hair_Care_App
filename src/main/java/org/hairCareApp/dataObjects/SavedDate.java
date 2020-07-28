package org.hairCareApp.dataObjects;

import org.hairCareApp.Product;

import java.util.ArrayList;
import java.util.List;
// Date which is stored in database
public class SavedDate {
    // Products that are assigned to date
    private final List<Product> products;
    private final String date;

    private SavedDate(String date) {
        this.date = date;
        products = new ArrayList<>();
    }
    public static SavedDate newSavedDate(String date) {
        return new SavedDate(date);
    }
    public String getDate() { return date; }
    public List<Product> getProducts() { return products; }
    public void addProduct(Product product) {
        // Product must not be assigned to date
        if(getProduct(product.getName()) == null)
            products.add(product);
    }
    public void removeProduct(Product product) {
        // If product is valid and assigned to date
        if(product != null && getProduct(product.getName()) != null)
            products.remove(product);
    }
    // Get specific product assigned to date
    public Product getProduct(String productName) {
        for(Product product : products)
            if(product.getName().equals(productName))
                return product;
            return null;
    }
}
