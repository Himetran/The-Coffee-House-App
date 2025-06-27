package com.example.thecoffeehouse.activity;

import static com.example.thecoffeehouse.constant.OrderStatus.PENDING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.CheckoutAdapter;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.CartTable;
import com.example.thecoffeehouse.database.Table.UserTable;
import com.example.thecoffeehouse.model.CartItem;
import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;
import com.example.thecoffeehouse.model.User;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtNewAddress;
    private RadioGroup radioPaymentMethod;
    private RecyclerView recyclerOrderItems;
    private TextView tvTotalAmount;
    private Button btnPlaceOrder;

    private Spinner spinner;

    private List<CartItem> cartItems = new ArrayList<>();

    private List<String> addressList = new ArrayList<>();

    private CheckoutAdapter checkoutAdapter;

    private DatabaseHelper databaseHelper;

    private SharedPreferences pref;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initView();
        loadData();
        updateTotalAmount();
        handleClick();
    }

    private void handleClick() {
        btnPlaceOrder.setOnClickListener(v -> handlePlaceOrder());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = addressList.get(position);
                if (selected.equals("Nhập địa chỉ mới")) {
                    edtNewAddress.setVisibility(View.VISIBLE);
                } else {
                    edtNewAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadData() {


        // Ví dụ danh sách địa chỉ đã lưu
        Cursor cursorUser = databaseHelper.getUserById(pref.getInt("userId", 1));
        if (Objects.nonNull(cursorUser) && cursorUser.moveToFirst()) {
                User user = new User(
                        cursorUser.getInt(cursorUser.getColumnIndexOrThrow(UserTable.COLUMN_ID)),
                        cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserTable.COLUMN_FULL_NAME)),
                        cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserTable.COLUMN_PHONE))
                );
            edtName.setText(user.getName());
            edtName.setEnabled(false);
            edtPhone.setText(user.getPhone());
            edtPhone.setEnabled(false);
            Cursor cursorAddress = databaseHelper.getAddressByUserId(pref.getInt("userId", 1));
            if (cursorAddress.moveToFirst()) {
                do {

                    addressList.add(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(UserTable.COLUMN_ADDRESS)));
                } while (cursorAddress.moveToNext());
            }
            cursorAddress.close();
        }
        cursorUser.close();
        addressList.add("Nhập địa chỉ mới");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Cursor cursor = databaseHelper.getAllCartByUserId(pref.getInt("userId", 1));
        if (cursor.moveToFirst()) {
            do {
                CartItem cart = new CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_QUANTITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_IMAGE))
                );
                cartItems.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        checkoutAdapter = new CheckoutAdapter(this, cartItems);
        recyclerOrderItems.setAdapter(checkoutAdapter);
    }

    private void initView() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        spinner = findViewById(R.id.spinnerSavedAddresses);
        edtNewAddress = findViewById(R.id.edtNewAddress);
        radioPaymentMethod = findViewById(R.id.radioPaymentMethod);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        databaseHelper = new DatabaseHelper(this);
        cartItems = new ArrayList<>();
        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void updateTotalAmount() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity() * item.getProductPrice();
        }
        tvTotalAmount.setText(String.format("%,dđ", total));
    }

    private void handlePlaceOrder() {
        int paymentMethodId = radioPaymentMethod.getCheckedRadioButtonId();
        String paymentMethod = (paymentMethodId == R.id.radioCOD) ? "COD" : "Online";

        String address;
        if (spinner.getSelectedItem().toString().equals("Nhập địa chỉ mới")) {
            address = edtNewAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ mới", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            address = spinner.getSelectedItem().toString();
        }

        Order order = new Order();
        order.setOrderDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        order.setCustomerName(edtName.getText().toString().trim());
        order.setCustomerPhone(edtPhone.getText().toString().trim());
        order.setDeliveryAddress(address);
        order.setOrderCode("OD" + generateRandomCode());
        int total = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem item : cartItems) {
            total += item.getQuantity() * item.getProductPrice();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderCode(order.getOrderCode());
            orderDetail.setProductId(item.getProductId());
            orderDetail.setOrderCode(order.getOrderCode());
            orderDetail.setQuantity(item.getQuantity());
            //TODO topping add sau
            orderDetails.add(orderDetail);
        }
        order.setTotalAmount(total);
        order.setStatus(PENDING.getValue());
        order.setUserId(pref.getInt("userId", 1));
        databaseHelper.insertOrder(order);
        orderDetails.forEach(orderDetail -> databaseHelper.insertOrderDetail(orderDetail));
        // Xóa giỏ hàng
        databaseHelper.deleteCart(pref.getInt("userId", 1));

        // Mở trang cảm ơn
        Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
        startActivity(intent);
        finish();
    }

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
