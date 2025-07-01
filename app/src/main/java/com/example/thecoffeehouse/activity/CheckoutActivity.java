package com.example.thecoffeehouse.activity;

import static com.example.thecoffeehouse.constant.OrderStatus.PENDING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.CheckoutAdapter;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.AddressTable;
import com.example.thecoffeehouse.database.Table.CartTable;
import com.example.thecoffeehouse.model.Address;
import com.example.thecoffeehouse.model.CartItem;
import com.example.thecoffeehouse.model.Order;
import com.example.thecoffeehouse.model.OrderDetail;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private RadioGroup radioPaymentMethod;
    private RecyclerView recyclerOrderItems;
    private TextView tvAddressInfo;
    private TextView tvTotalAmount;
    private Button btnPlaceOrder;
    private List<CartItem> cartItems = new ArrayList<>();
    private List<Address> addressList = new ArrayList<>();
    private CheckoutAdapter checkoutAdapter;
    private DatabaseHelper databaseHelper;
    private SharedPreferences pref;
    private Address addressSelected;

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

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
        tvAddressInfo.setOnClickListener(v -> {
            if (addressList.isEmpty()) {
                showAddNewAddressDialog();
            } else {
                showReceiverSelectionDialog(addressList);
            }
        });
    }

    private void showAddNewAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm địa chỉ mới");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtAddress = view.findViewById(R.id.edtAddress);

        builder.setView(view);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Address newAddress = new Address();
            newAddress.setPhone(phone);
            newAddress.setName(name);
            newAddress.setAddress(address);
            newAddress.setUserId(pref.getInt("userId", 1));
            addressList.add(newAddress);
            databaseHelper.insertAddress(newAddress);
            updateAddressInfo(newAddress);
            addressSelected = newAddress;

            Toast.makeText(this, "Đã thêm địa chỉ mới", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showReceiverSelectionDialog(List<Address> addressList) {
        String[] names = new String[addressList.size()];
        for (int i = 0; i < addressList.size(); i++) {
            names[i] = addressList.get(i).getName() + " - " + addressList.get(i).getPhone();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn người nhận");
        builder.setItems(names, (dialog, which) -> {
            addressSelected = addressList.get(which);
            updateAddressInfo(addressSelected);
        });
        builder.setNegativeButton("Thêm mới", (dialog, which) -> showAddNewAddressDialog());
        builder.show();
    }

    private void loadData() {
        Cursor cursorAddress = databaseHelper.getAddressByUserId(pref.getInt("userId", 1));
        if (cursorAddress.moveToFirst()) {
            do {
                Address address = new Address();
                address.setUserId(cursorAddress.getInt(cursorAddress.getColumnIndexOrThrow(AddressTable.COLUMN_USER_ID)));
                address.setName(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(AddressTable.COLUMN_NAME)));
                address.setAddress(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(AddressTable.COLUMN_ADDRESS)));
                addressList.add(address);
            } while (cursorAddress.moveToNext());
        }
        cursorAddress.close();
        if (!addressList.isEmpty()) {
            addressSelected = addressList.get(0);
            updateAddressInfo(addressSelected);
        }


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
        radioPaymentMethod = findViewById(R.id.radioPaymentMethod);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        databaseHelper = new DatabaseHelper(this);
        cartItems = new ArrayList<>();
        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        tvAddressInfo = findViewById(R.id.tvReceiverInfo);
    }

    private void updateAddressInfo(Address receiver) {
        String info = receiver.getName() + "\n" +
                receiver.getPhone() + "\n" +
                receiver.getAddress();
        tvAddressInfo.setText(info);
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
        Order order = new Order();
        order.setOrderDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        order.setCustomerName(addressSelected.getName());
        order.setCustomerPhone(addressSelected.getPhone());
        order.setDeliveryAddress(addressSelected.getAddress());
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

}
