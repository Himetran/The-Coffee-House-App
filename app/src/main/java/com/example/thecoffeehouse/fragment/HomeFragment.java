package com.example.thecoffeehouse.fragment;

import android.database.Cursor;
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
import com.example.thecoffeehouse.adapter.HotTrendAdapter;
import com.example.thecoffeehouse.adapter.ProductAdapter;
import com.example.thecoffeehouse.adapter.ServiceAdapter;
import com.example.thecoffeehouse.database.DatabaseHelper;
import com.example.thecoffeehouse.database.Table.ProductTable;
import com.example.thecoffeehouse.manage.CartManager;
import com.example.thecoffeehouse.model.CartItem;
import com.example.thecoffeehouse.model.HotTrend;
import com.example.thecoffeehouse.model.Product;
import com.example.thecoffeehouse.model.Service;
import com.example.thecoffeehouse.popup.BottomSheetCart;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerService, recyclerProduct;
    private ViewPager2 viewPagerHotTrend;
    private TextView tvGreeting;
    private ImageButton btnNotification, btnPromotion;

    private View stickyHeader;
    private TextView tvCategorySticky;
    private ImageButton btnSearchSticky;

    private NestedScrollView scrollView;

    private DatabaseHelper databaseHelper;

    private View cartIconLayout; // giỏ hàng sẽ hiện khi có sp
    private TextView tvCartCount;

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
        Cursor cursor = databaseHelper.findProduct(null);
        if (cursor.moveToFirst()) {
            do {
                Product user = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_SALE_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IS_BESTSELLER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IS_RECOMMENDED)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IS_AVAILABLE))
                );
                products.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Dịch vụ
        recyclerService.setAdapter(new ServiceAdapter(getContext(), Service.getDummyData()));

        // Hot trend
        viewPagerHotTrend.setAdapter(new HotTrendAdapter(getContext(), HotTrend.getDummyData()));

        // Sản phẩm
        ProductAdapter productAdapter = new ProductAdapter(getContext(), products, (product, quantity) -> {
            CartManager.getInstance().addToCart(product, quantity);
            //TODO kiểm tra xem thằng cu này có giỏ hàng hay chưa nếu chưa thì tạo mới còn có thì cập nhật lại vào database
            Toast.makeText(getContext(), "Đã thêm " + quantity + " sản phẩm vào giỏ", Toast.LENGTH_SHORT).show();
            updateCartUI();
        });
        recyclerProduct.setAdapter(productAdapter);
    }

    private void updateCartUI() {
        int total = CartManager.getInstance().getTotalItemCount();
        if (total > 0) {
            tvCartCount.setText(String.valueOf(total));
            cartIconLayout.setVisibility(View.VISIBLE);
            tvCartCount.setVisibility(View.VISIBLE);
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
        tvGreeting.setText("Xin chào, Long!");

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
            // Giả lập danh sách sản phẩm trong giỏ
            List<CartItem> cartList = new ArrayList<>();
            cartList.add(new CartItem(1, 2, "long", 1, 1));
            cartList.add(new CartItem(1, 2, "long", 1, 1));

            cartList.add(new CartItem(1, 2, "long", 1, 1));

            BottomSheetCart sheet = new BottomSheetCart(cartList);
            sheet.show(getParentFragmentManager(), "CartBottomSheet");
        });
    }

    private void initView(View view) {
        databaseHelper = new DatabaseHelper(getContext());
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

        cartIconLayout = view.findViewById(R.id.cartFloatingButton);
        tvCartCount = view.findViewById(R.id.tvCartCount);
        cartIconLayout.setVisibility(View.GONE);

        recyclerService.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerProduct.setNestedScrollingEnabled(false);
    }
}
