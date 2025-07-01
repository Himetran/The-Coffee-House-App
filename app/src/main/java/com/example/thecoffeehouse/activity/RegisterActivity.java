package com.example.thecoffeehouse.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.getUserByPhone(phone);
        if (cursor.moveToFirst()) {
            cursor.close();
            Toast.makeText(this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }
        cursor.close();

        User user = new User();
        user.setName(name);
        user.setPhone(phone);

        db.insertUser(user, password);
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}
