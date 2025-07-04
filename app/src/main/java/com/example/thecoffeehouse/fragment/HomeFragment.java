package com.example.thecoffeehouse.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.activity.LoginActivity;
import com.example.thecoffeehouse.adapter.HotTrendAdapter;
import com.example.thecoffeehouse.adapter.ProductAdapter;
import com.example.thecoffeehouse.adapter.ServiceAdapter;
import com.example.thecoffeehouse.model.CartItem;
import com.example.thecoffeehouse.model.HotTrend;
import com.example.thecoffeehouse.model.Product;
import com.example.thecoffeehouse.model.Service;
import com.example.thecoffeehouse.popup.BottomSheetCart;
import com.example.thecoffeehouse.service.ApiClient;
import com.example.thecoffeehouse.service.CartService;
import com.example.thecoffeehouse.service.ProductService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    List<CartItem> cartList = new ArrayList<>();
    ProductService productService = ApiClient.getClient().create(ProductService.class);
    CartService cartService = ApiClient.getClient().create(CartService.class);
    private RecyclerView recyclerService, recyclerProduct;
    private ViewPager2 viewPagerHotTrend;
    private TextView tvGreeting;
    private ImageButton btnNotification, btnPromotion;
    private View stickyHeader;
    private TextView tvCategorySticky;
    private ImageButton btnSearchSticky;
    private NestedScrollView scrollView;
    private MaterialButton cartIconLayout;
    private SharedPreferences pref;
    private List<Product> products = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        loadData();
        updateCartUI();
        handleEvents();
        return view;
    }


    private void loadData() {
        productService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                products = response.body();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


        // Dịch vụ
        recyclerService.setAdapter(new ServiceAdapter(getContext(), Service.getDummyData()));

        // Hot trend
        viewPagerHotTrend.setAdapter(new HotTrendAdapter(getContext(), HotTrend.getDummyData()));

        // Sản phẩm
        ProductAdapter productAdapter = new ProductAdapter(getContext(), products, this::onAddToCart);
        recyclerProduct.setAdapter(productAdapter);


    }

    private void onAddToCart(Product product, int quantity) {
        if (pref.getBoolean("isLoggedIn", false)) {
            //TODO: Nhớ viết thêm api nha Long
//            Cursor cursor = productService.getAllCartByUserIdAndProductId(pref.getInt("userId", 1), product.getProduct_id());
//            if (cursor == null || !cursor.moveToFirst()) {
//                databaseHelper.insertCart(pref.getInt("userId", 1), quantity, product.getProduct_id());
//            }else {
//                CartItem cart = new CartItem(
//                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_ID)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_USER_ID)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_QUANTITY)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_ID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_NAME)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_PRICE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_IMAGE))
//                );
//                databaseHelper.updateCart(cart.getId(), product.getProduct_id(), cart.getQuantity() + quantity, pref.getInt("userId", 1));
//            }
            updateCartUI();
        } else {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private void updateCartUI() {
        int totalItem = 0;
        if (pref.getBoolean("isLoggedIn", false)) {
            //TODO: Nhớ viết api nha Long
//            Cursor cursor = cartService.getCountCart(pref.getInt("userId", 1));
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    totalItem = cursor.getInt(0);
//                }
//                cursor.close();
//            }
        }
        if (totalItem > 0) {
            cartIconLayout.setText(String.valueOf(totalItem));
            cartIconLayout.setVisibility(View.VISIBLE);
        } else {
            cartIconLayout.setVisibility(View.GONE);
        }
    }


    public void handleEvents() {
        // Sticky header
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            if (scrollY > 400) {
                stickyHeader.setVisibility(View.VISIBLE);
            } else {
                stickyHeader.setVisibility(View.GONE);
            }
        });

        // Header greeting
        tvGreeting.setText("Xin chào, Hime!");

        btnNotification.setOnClickListener(v ->
                Toast.makeText(getContext(), "Bạn chưa có thông báo nào", Toast.LENGTH_SHORT).show()
        );

        btnPromotion.setOnClickListener(v ->
                Toast.makeText(getContext(), "Khuyến mãi hấp dẫn đang chờ bạn!", Toast.LENGTH_SHORT).show()
        );

        tvCategorySticky.setOnClickListener(v ->
                Toast.makeText(getContext(), "Mở danh mục (popup)", Toast.LENGTH_SHORT).show()
        );

        btnSearchSticky.setOnClickListener(v ->
                Toast.makeText(getContext(), "Tìm kiếm sản phẩm", Toast.LENGTH_SHORT).show()
        );

        cartIconLayout.setOnClickListener(v -> {

            cartService.getCartByUserId(String.valueOf(pref.getInt("userId", 1))).enqueue(new Callback<List<CartItem>>() {
                @Override
                public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                    cartList = response.body();
                }

                @Override
                public void onFailure(Call<List<CartItem>> call, Throwable t) {

                }
            });


            BottomSheetCart sheet = new BottomSheetCart(cartList, this::updateCartUI);
            sheet.show(getParentFragmentManager(), "CartBottomSheet");
        });
    }

    private void initView(View view) {
        recyclerService = view.findViewById(R.id.recyclerService);
        recyclerProduct = view.findViewById(R.id.recyclerProduct);
        viewPagerHotTrend = view.findViewById(R.id.viewPagerHotTrend);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        btnNotification = view.findViewById(R.id.btnNotification);
        btnPromotion = view.findViewById(R.id.btnPromotion);

        stickyHeader = view.findViewById(R.id.stickyHeader);
        tvCategorySticky = view.findViewById(R.id.tvCategorySticky);
        btnSearchSticky = view.findViewById(R.id.btnSearchSticky);
        scrollView = view.findViewById(R.id.scrollViewContent);

        cartIconLayout = view.findViewById(R.id.btnCartFloating);
        cartIconLayout.setVisibility(View.GONE);
        pref = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        recyclerService.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerProduct.setNestedScrollingEnabled(false);
    }
}
