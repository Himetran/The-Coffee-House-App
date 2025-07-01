package com.example.thecoffeehouse.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.databinding.ActivityMainBinding;
import com.example.thecoffeehouse.fragment.HomeFragment;
import com.example.thecoffeehouse.fragment.OrderFragment;
import com.example.thecoffeehouse.fragment.OtherFragment;
import com.example.thecoffeehouse.fragment.PromoteFragment;
import com.example.thecoffeehouse.fragment.StoreFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        initView();
    }

    private void initView() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_home){
                replaceFragment(new HomeFragment());
            } else if(item.getItemId() == R.id.nav_order){
                replaceFragment(new OrderFragment());
            } else if(item.getItemId() == R.id.nav_store){
                replaceFragment(new StoreFragment());
            } else if(item.getItemId() == R.id.nav_promote){
                replaceFragment(new PromoteFragment());
            } else {
                replaceFragment(new OtherFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }
}


