package com.example.thecoffeehouse.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.OrderHistoryAdapter;
import com.example.thecoffeehouse.adapter.OrderItemAdapter;
import com.example.thecoffeehouse.constant.OrderStatus;
import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;
import com.example.thecoffeehouse.service.ApiClient;
import com.example.thecoffeehouse.service.OrderService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    List<OrderDetail> orderDetails = new ArrayList<>();
    OrderService orderService = ApiClient.getClient().create(OrderService.class);
    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        orderList = new ArrayList<>();
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);

        orderService.getOrdersByUser(String.valueOf(pref.getInt("userId", 1))).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                orderList = response.body();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });


        adapter = new OrderHistoryAdapter(orderList, this::showOrderDetailDialog);
        rvOrderHistory.setAdapter(adapter);
    }

    private void showOrderDetailDialog(Order order) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_order_detail, null);

        // Thông tin đơn hàng
        TextView tvStatus = view.findViewById(R.id.tvOrderStatus);
        TextView tvTotal = view.findViewById(R.id.tvTotalAmount);

        // Thông tin người nhận
        TextView tvCustomerName = view.findViewById(R.id.tvCustomerName);
        TextView tvCustomerPhone = view.findViewById(R.id.tvCustomerPhone);
        TextView tvDeliveryAddress = view.findViewById(R.id.tvDeliveryAddress);

        // Phương thức thanh toán
        TextView tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);


        orderService.getOrderDetails(order.getOrderCode()).enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                orderDetails = response.body();
            }

            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {

            }
        });


        // Danh sách sản phẩm
        RecyclerView recyclerView = view.findViewById(R.id.recyclerOrderItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OrderItemAdapter(this, orderDetails));

        // Set dữ liệu
        tvStatus.setText("Trạng thái: " + OrderStatus.getDisplayNameFromValue(order.getStatus()));
        tvTotal.setText(order.getTotalAmount() + "đ");

        tvCustomerName.setText("Họ tên: " + order.getCustomerName());
        tvCustomerPhone.setText("SĐT: " + order.getCustomerPhone());
        tvDeliveryAddress.setText("Địa chỉ: " + order.getDeliveryAddress());

        dialog.setContentView(view);
        dialog.show();
    }

}
