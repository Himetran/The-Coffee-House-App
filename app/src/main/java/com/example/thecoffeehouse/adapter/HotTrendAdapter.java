package com.example.thecoffeehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.model.HotTrendModel;

import java.util.List;

public class HotTrendAdapter extends RecyclerView.Adapter<HotTrendAdapter.HotTrendViewHolder> {

    private final List<HotTrendModel> hotTrendList;
    private final Context context;

    public HotTrendAdapter(Context context, List<HotTrendModel> hotTrendList) {
        this.context = context;
        this.hotTrendList = hotTrendList;
    }

    @NonNull
    @Override
    public HotTrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_trend, parent, false);
        return new HotTrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotTrendViewHolder holder, int position) {
        HotTrendModel model = hotTrendList.get(position);
        holder.imgHotTrend.setImageResource(model.getImageResId());
        holder.tvHotTrendTitle.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return hotTrendList.size();
    }

    static class HotTrendViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotTrend;
        TextView tvHotTrendTitle;

        public HotTrendViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotTrend = itemView.findViewById(R.id.imgHotTrend);
            tvHotTrendTitle = itemView.findViewById(R.id.tvHotTrendTitle);
        }
    }
}
