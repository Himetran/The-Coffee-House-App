package com.example.thecoffeehouse.model;

import com.example.thecoffeehouse.R;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private String name;
    private String imageUrl;

    public Service(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }

    public String getImageUrl() {
        return imageUrl;
    }

    public static List<Service> getDummyData() {
        List<Service> list = new ArrayList<>();
        list.add(new Service("Giao hàng", "https://static.chotot.com/storage/chotot-kinhnghiem/c2c/2021/12/b3d01a73-lam-shipper-cho-cong-ty-nao-tot-nhat-thumb.jpeg"));
        list.add(new Service("Mang đi", "https://20sfvn.com/wp-content/uploads/2022/12/cafe-take-away-la-gi.jpeg"));
        list.add(new Service("Tại chỗ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4917CFZvUjEOpDid02gMfHzf4WgJhv8KfMQ&s"));
        return list;
    }
}

