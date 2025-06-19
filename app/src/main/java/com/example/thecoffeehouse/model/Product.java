package com.example.thecoffeehouse.model;

import com.example.thecoffeehouse.R;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private int product_id;
    private String name;

    private String description;
    private double price;
    private double salePrice;
    private String imageUrl;

    private int categoryId;

    private int isBestseller;

    private int isRecommended;

    private int isAvailable;

    private List<ProductOption> options;


    public Product() {
    }

    public Product(int product_id, String name, String description, double price, double salePrice, String imageUrl, int categoryId, int isBestseller, int isRecommended, int isAvailable) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.isBestseller = isBestseller;
        this.isRecommended = isRecommended;
        this.isAvailable = isAvailable;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(int isBestseller) {
        this.isBestseller = isBestseller;
    }

    public int getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(int isRecommended) {
        this.isRecommended = isRecommended;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }

    public List<ProductOption> getOptions() {
        return options;
    }

    public void setOptions(List<ProductOption> options) {
        this.options = options;
    }



}
