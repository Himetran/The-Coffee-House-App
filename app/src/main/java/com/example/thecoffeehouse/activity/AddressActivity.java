package com.example.thecoffeehouse.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.adapter.AddressAdapter;
import com.example.thecoffeehouse.model.Address;
import com.example.thecoffeehouse.service.AddressService;
import com.example.thecoffeehouse.service.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {

    AddressService addressService = ApiClient.getClient().create(AddressService.class);
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<Address> addressList;
    private Button btnAdd;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();
        loadData();

        adapter = new AddressAdapter(addressList, new AddressAdapter.OnAddressActionListener() {
            @Override
            public void onEdit(int position) {
                showAddAddressDialog(false, position);
            }

            @Override
            public void onDelete(int position) {
                showDeleteAddressDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(v -> showAddAddressDialog(true, 0));
    }

    private void showDeleteAddressDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá địa chỉ này không?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    Address address = addressList.get(position);
                    addressService.deleteAddress(String.valueOf(address.getId())).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(AddressActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            addressList.clear();
                            loadData();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void showAddAddressDialog(boolean isCreate, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm địa chỉ mới");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        builder.setView(view);
        if (!isCreate) {
            edtName.setText(addressList.get(index).getName());
            edtAddress.setText(addressList.get(index).getAddress());
            edtPhone.setText(addressList.get(index).getPhone());
        }
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Address entity = new Address();
                if (isCreate) {
                    entity.setAddress(address);
                    entity.setName(name);
                    entity.setPhone(phone);
                    entity.setUserId(pref.getInt("userId", 1));
                    addressService.createAddress(entity).enqueue(new Callback<Address>() {
                        @Override
                        public void onResponse(Call<Address> call, Response<Address> response) {
                            Toast.makeText(AddressActivity.this, "Sửa địa chỉ thành công", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Address> call, Throwable t) {

                        }
                    });
                } else {
                    entity = addressList.get(index);
                    entity.setAddress(address);
                    entity.setName(name);
                    entity.setPhone(phone);
                    addressService.updateAddress(String.valueOf(entity.getId()), entity).enqueue(new Callback<Address>() {
                        @Override
                        public void onResponse(Call<Address> call, Response<Address> response) {
                            Toast.makeText(AddressActivity.this, "Thêm địa chỉ thành công", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Address> call, Throwable t) {

                        }
                    });
                }
                addressList.clear();
                loadData();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadData() {
        addressService.getAddressByUserId(String.valueOf(pref.getInt("userId", 1))).enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                addressList = response.body();
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {

            }
        });

    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerSavedAddresses);
        btnAdd = findViewById(R.id.btnAddAddress);
        addressList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
    }
}
