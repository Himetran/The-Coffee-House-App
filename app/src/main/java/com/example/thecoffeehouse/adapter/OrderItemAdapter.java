package com.example.thecoffeehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.model.OrderDetail;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderDetail> itemList;


    public OrderItemAdapter(Context context, List<OrderDetail> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderDetail item = itemList.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvQuantity.setText("Số lượng: x" + item.getQuantity());
        holder.tvPrice.setText("Giá: " + item.getProductPrice() + "đ");

        // Load ảnh nếu có
        Glide.with(context)
                .load(item.getProductImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvQuantity, tvPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
