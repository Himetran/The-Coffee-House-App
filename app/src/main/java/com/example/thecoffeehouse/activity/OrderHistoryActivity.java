package com.example.thecoffeehouse.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.OrderHistoryAdapter;
import com.example.thecoffeehouse.adapter.OrderItemAdapter;
import com.example.thecoffeehouse.constant.OrderStatus;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.OrderDetailTable;
import com.example.thecoffeehouse.database.Table.OrderTable;
import com.example.thecoffeehouse.database.Table.ProductTable;
import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;
import com.example.thecoffeehouse.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;

    private DatabaseHelper databaseHelper;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        orderList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);

        //TODO load từ db lên
        Cursor cursor = databaseHelper.getOrderByUserId(pref.getInt("userId", 1));
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderCode(cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_ORDER_CODE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_TOTAL_AMOUNT)));
                order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_ORDER_DATE)));
                order.setDeliveryAddress(cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_DELIVERY_ADDRESS)));
                order.setCustomerPhone(cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_CUSTOMER_PHONE)));
                order.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_CUSTOMER_NAME)));
                order.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_STATUS)));
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();

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

        //load orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        Cursor cursor = databaseHelper.getOrderDetailByOrderCode(order.getOrderCode());
        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(OrderDetailTable.COLUMN_QUANTITY)));
                orderDetail.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(OrderDetailTable.COLUMN_PRODUCT_NAME)));
                orderDetail.setProductPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(OrderDetailTable.COLUMN_PRODUCT_PRICE)));
                orderDetail.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(OrderDetailTable.COLUMN_PRODUCT_IMAGE_URL)));
                orderDetails.add(orderDetail);
            } while (cursor.moveToNext());
        }

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
