package com.example.evidencia_final_app;

public class Items {
    private String itemName;
    private String itemCategory;
    private String itemPrice;
    private String itemBarcode;

    public Items() {

    }

    public Items(String itemName, String itemCategory, String itemPrice, String itemBarcode) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemBarcode = itemBarcode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }
}
