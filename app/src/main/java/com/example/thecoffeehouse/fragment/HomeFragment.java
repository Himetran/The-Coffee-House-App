package com.example.thecoffeehouse.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
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
import com.example.thecoffeehouse.model.HotTrendModel;
import com.example.thecoffeehouse.model.ProductModel;
import com.example.thecoffeehouse.model.ServiceModel;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerService, recyclerProduct;
    private ViewPager2 viewPagerHotTrend;
    private TextView tvGreeting;
    private ImageButton btnNotification, btnPromotion;

    private View stickyHeader;
    private TextView tvCategorySticky;
    private ImageButton btnSearchSticky;
    private NestedScrollView scrollView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Ánh xạ view
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

        // 2. Xử lý sticky header khi scroll
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            if (scrollY > 400) {
                stickyHeader.setVisibility(View.VISIBLE);
            } else {
                stickyHeader.setVisibility(View.GONE);
            }
        });

        // 3. Header sự kiện
        tvGreeting.setText("Xin chào, Long!");

        btnNotification.setOnClickListener(v ->
                Toast.makeText(getContext(), "Bạn chưa có thông báo nào", Toast.LENGTH_SHORT).show()
        );

        btnPromotion.setOnClickListener(v ->
                Toast.makeText(getContext(), "Khuyến mãi hấp dẫn đang chờ bạn!", Toast.LENGTH_SHORT).show()
        );

        // 4. Sticky header event
        tvCategorySticky.setOnClickListener(v ->
                Toast.makeText(getContext(), "Mở danh mục (popup)", Toast.LENGTH_SHORT).show()
        );

        btnSearchSticky.setOnClickListener(v ->
                Toast.makeText(getContext(), "Tìm kiếm sản phẩm", Toast.LENGTH_SHORT).show()
        );

        // 5. Dịch vụ
        recyclerService.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerService.setAdapter(new ServiceAdapter(getContext(), ServiceModel.getDummyData()));

        // 6. Hot trend
        viewPagerHotTrend.setAdapter(new HotTrendAdapter(getContext(), HotTrendModel.getDummyData()));

        // 7. Sản phẩm
        recyclerProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerProduct.setNestedScrollingEnabled(false);
        recyclerProduct.setAdapter(new ProductAdapter(getContext(), ProductModel.getDummyData()));

        return view;
    }
}
