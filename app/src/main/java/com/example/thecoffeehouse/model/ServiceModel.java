package com.example.thecoffeehouse.model;

import com.example.thecoffeehouse.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceModel {
    private final String name;
    private final int iconResId;

    public ServiceModel(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }

    public static List<ServiceModel> getDummyData() {
        List<ServiceModel> list = new ArrayList<>();
        list.add(new ServiceModel("Giao hàng", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Mang đi", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Tại chỗ", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Giao hàng", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Mang đi", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Tại chỗ", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Giao hàng", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Mang đi", R.drawable.ic_launcher_background));
        list.add(new ServiceModel("Tại chỗ", R.drawable.ic_launcher_background));
        return list;
    }
}

