package com.example.thecoffeehouse.model;

public class CartItem {
    private int userId;
    private int quantity;

    private int productId;



    private String productName;

    private double productPrice;


    public CartItem(int userId, double productPrice, String productName, int productId, int quantity) {
        this.userId = userId;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productId = productId;
        this.quantity = quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
