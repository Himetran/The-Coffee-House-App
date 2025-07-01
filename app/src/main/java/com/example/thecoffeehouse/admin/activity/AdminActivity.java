package com.example.thecoffeehouse.admin.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.admin.fragment.DashboardFragment;
import com.example.thecoffeehouse.admin.fragment.OrderManagementFragment;
import com.example.thecoffeehouse.admin.fragment.ProductManagementFragment;
import com.example.thecoffeehouse.admin.fragment.RevenueFragment;
import com.example.thecoffeehouse.admin.fragment.UserManagementFragment;
import com.example.thecoffeehouse.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new DashboardFragment());
        initView();
    }

    private void initView() {
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_home){
                replaceFragment(new DashboardFragment());
            } else if(item.getItemId() == R.id.nav_order){
                replaceFragment(new OrderManagementFragment());
            } else if(item.getItemId() == R.id.nav_user){
                replaceFragment(new UserManagementFragment());
            } else if(item.getItemId() == R.id.nav_revenue){
                replaceFragment(new RevenueFragment());
            } else {
                replaceFragment(new ProductManagementFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewAdmin, fragment).commit();
    }
}
