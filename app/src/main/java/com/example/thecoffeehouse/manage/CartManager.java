package com.example.thecoffeehouse.manage;

import com.example.thecoffeehouse.model.Product;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static final CartManager instance = new CartManager();
    private final Map<Product, Integer> cart = new HashMap<>();

    private CartManager() {}

    public static CartManager getInstance() {
        return instance;
    }

    public void addToCart(Product product, int quantity) {
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
    }

    public Map<Product, Integer> getCartItems() {
        return cart;
    }

    public int getTotalItemCount() {
        int total = 0;
        for (int qty : cart.values()) {
            total += qty;
        }
        return total;
    }
}
