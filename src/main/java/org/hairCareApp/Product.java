package org.hairCareApp;

public class Product {
    private final String name;
    private final String type;
    private final String peh;
    private final String purchaseDate;

    private Product(String name, String type, String peh, String purchaseDate) {
        this.name = name;
        this.type = type;
        this.peh = peh;
        this.purchaseDate = purchaseDate;
    }
    public static Product newProduct(String name, String type, String peh, String purchaseDate) {
        return new Product(name, type, peh, purchaseDate);
    }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getPeh() { return peh; }
    public String getPurchaseDate() { return purchaseDate; }

    @Override
    public String toString() {
        return getName();
    }
}
