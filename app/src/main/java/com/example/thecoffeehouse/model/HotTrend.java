package com.example.thecoffeehouse.model;

import com.example.thecoffeehouse.R;

import java.util.ArrayList;
import java.util.List;

public class HotTrend {
    private final int imageResId;
    private final String title;

    public HotTrend(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }

    public static List<HotTrend> getDummyData() {
        List<HotTrend> list = new ArrayList<>();
        list.add(new HotTrend(R.drawable.bg_edit_text, "Mua 1 tặng 1"));
        list.add(new HotTrend(R.drawable.bg_edit_text, "Trà sữa hot nhất tuần"));
        list.add(new HotTrend(R.drawable.bg_edit_text, "Giảm 30% cho đơn đầu tiên"));
        return list;
    }
}
