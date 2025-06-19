package com.example.thecoffeehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thecoffeehouse.model.Service;

import java.util.List;
import android.content.Context;
import com.example.thecoffeehouse.R;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private final List<Service> serviceList;
    private final Context context;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.tvServiceName.setText(service.getName());
        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(service.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // ảnh tạm khi chờ load
                .error(R.drawable.bg_count_badge)             // ảnh khi load lỗi
                .into(holder.imgService);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView tvServiceName;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
        }
    }
}

