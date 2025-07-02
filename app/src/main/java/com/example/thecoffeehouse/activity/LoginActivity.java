package com.example.thecoffeehouse.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.model.User;
import com.example.thecoffeehouse.service.ApiClient;
import com.example.thecoffeehouse.service.UserService;
import com.example.thecoffeehouse.service.request.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnLogin, btnSignin;
    UserService userService = ApiClient.getClient().create(UserService.class);
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

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setPhone(phone);
            loginRequest.setPassword(password);

            userService.login(loginRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("userId", user.getId());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi hệ thống vui lòng thử lại", Toast.LENGTH_SHORT).show();

                }
            });
        });
    }

    private void initView() {
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignin = findViewById(R.id.btnSignin);
        pref = getSharedPreferences("login", MODE_PRIVATE);
    }
}
