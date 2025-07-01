package com.example.thecoffeehouse.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.admin.activity.AdminActivity;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.UserTable;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnLogin, btnSignin;
    DatabaseHelper dbHelper;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        handleLoginBtn();
        handleForgotPasswordBtn();
        handleSignupBtn();
    }

    private void handleSignupBtn() {
        btnSignin.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void handleForgotPasswordBtn() {
    }

    private void handleLoginBtn() {
        btnLogin.setOnClickListener(view -> {
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = dbHelper.checkUsernamePassword(phone, password);
            if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
                SharedPreferences.Editor editor = pref.edit();
                if (cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_PHONE)).equals("0969864591")) {
                    Intent intent = new Intent(this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("userId", cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)));
                    editor.apply();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignin = findViewById(R.id.btnSignin);
        pref = getSharedPreferences("login", MODE_PRIVATE);
        dbHelper = new DatabaseHelper(this);
    }

    private boolean checkLogin(String phone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE phone = ? AND password = ?",
                new String[]{phone, password}
        );

        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        return isLoggedIn;
    }
}
