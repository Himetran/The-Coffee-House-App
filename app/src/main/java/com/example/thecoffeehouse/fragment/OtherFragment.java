package com.example.thecoffeehouse.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.activity.AddressActivity;
import com.example.thecoffeehouse.activity.LoginActivity;
import com.example.thecoffeehouse.activity.MainActivity;
import com.example.thecoffeehouse.activity.OrderHistoryActivity;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.UserTable;
import com.example.thecoffeehouse.model.User;

import java.util.Objects;

public class OtherFragment extends Fragment {

    private TextView tvUserName, tvPhone, tvOrderHistory, tvSavedAddresses, tvLogout;

    private Button btnLogin;

    private DatabaseHelper databaseHelper;
    private SharedPreferences pref;

    public OtherFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadUserInfo();
        setupActions();
    }

    private void initView(View view) {
        tvUserName = view.findViewById(R.id.tvUsername);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvOrderHistory = view.findViewById(R.id.tvOrderHistory);
        tvSavedAddresses = view.findViewById(R.id.tvSavedAddresses);
        tvLogout = view.findViewById(R.id.tvLogout);
        btnLogin = view.findViewById(R.id.btnLogin);
        databaseHelper = new DatabaseHelper(requireContext());
        pref = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void loadUserInfo() {
        int userId = pref.getInt("userId", -1);
        tvUserName.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
        tvOrderHistory.setVisibility(View.VISIBLE);
        tvSavedAddresses.setVisibility(View.VISIBLE);
        tvLogout.setVisibility(View.VISIBLE);
        if (userId == -1) {
            tvUserName.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tvOrderHistory.setVisibility(View.GONE);
            tvSavedAddresses.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }


        Cursor cursor = databaseHelper.getUserById(userId);
        if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_PHONE))
            );
            tvUserName.setText(user.getName());
            tvPhone.setText(user.getPhone());
        }

    }

    private void setupActions() {
        tvOrderHistory.setOnClickListener(v -> startActivity(new Intent(getContext(), OrderHistoryActivity.class)));

        tvSavedAddresses.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressActivity.class)));

        tvLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
