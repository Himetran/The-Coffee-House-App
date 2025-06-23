package com.example.thecoffeehouse.listener;


import com.example.thecoffeehouse.model.Product;

public interface OnAddToCartListener {
    void onAddToCart(Product product, int quantity);
}
