package com.example.thecoffeehouse.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.OrderHistoryAdapter;
import com.example.thecoffeehouse.model.Order;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        orderList = new ArrayList<>();

        orderList.add(new Order(1, "24/06/2025", "Đang giao", 500.00));
        orderList.add(new Order(2, "20/06/2025", "Hoàn tất", 700.000));
        orderList.add(new Order(3, "19/06/2025", "Đã hủy", 0));

        adapter = new OrderHistoryAdapter(orderList, order -> showOrderDetailDialog(order));
        rvOrderHistory.setAdapter(adapter);
    }

    private void showOrderDetailDialog(Order order) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_order_detail, null);

        TextView tvCode = view.findViewById(R.id.tvDetailCode);
        TextView tvDate = view.findViewById(R.id.tvDetailDate);
        TextView tvStatus = view.findViewById(R.id.tvDetailStatus);
        TextView tvTotal = view.findViewById(R.id.tvDetailTotal);

        tvCode.setText("Mã đơn: #" + order.getOrderId());
        tvDate.setText("Ngày đặt: " + order.getOrderDate());
        tvStatus.setText("Trạng thái: " + order.getStatus());
        tvTotal.setText("Tổng tiền: " + order.getTotalAmount() + "đ");

        dialog.setContentView(view);
        dialog.show();
    }
}
