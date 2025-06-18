package com.example.thecoffeehouse.model;

import com.example.thecoffeehouse.R;

import java.util.ArrayList;
import java.util.List;

public class ProductModel {
    private final String name;
    private final String price;
    private final int imageResId;

    private List<ProductOption> options;

    public ProductModel(String name, String price, int imageResId, List<ProductOption> options) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.options = options;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }

    public static List<ProductModel> getDummyData() {
        List<ProductModel> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ProductModel("Trà sữa trân châu", "45.000đ", R.drawable.button_primary, List.of(new ProductOption("test", ProductOption.OptionType.CHECKBOX, List.of("hehe")))));
        }
        return list;
    }
}
