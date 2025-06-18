package com.example.thecoffeehouse.model;

import java.util.List;

public class ProductOption {
    public enum OptionType {
        RADIO, CHECKBOX, TEXT
    }

    private String name;
    private OptionType type;
    private List<String> values;

    public ProductOption(String name, OptionType type, List<String> values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }

    // Getters
    public String getName() { return name; }
    public OptionType getType() { return type; }
    public List<String> getValues() { return values; }
}
