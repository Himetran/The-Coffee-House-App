package com.example.thecoffeehouse.popup;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.model.ProductOption;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductOptionBottomSheet extends BottomSheetDialogFragment {
    private final String productName;
    private final List<ProductOption> options;

    public ProductOptionBottomSheet(String productName, List<ProductOption> options) {
        this.productName = productName;
        this.options = options;
    }

    private int quantity = 1;
    private final Map<String, String> selectedOptions = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        scrollView.addView(layout);

        // Tiêu đề
        TextView title = new TextView(getContext());
        title.setText("Tùy chọn cho: " + productName);
        title.setTextSize(18);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);

        for (ProductOption option : options) {
            TextView tv = new TextView(getContext());
            tv.setText(option.getName());
            tv.setTextSize(16);
            layout.addView(tv);

            switch (option.getType()) {
                case RADIO:
                    RadioGroup rg = new RadioGroup(getContext());
                    rg.setOrientation(RadioGroup.HORIZONTAL);
                    for (String val : option.getValues()) {
                        RadioButton rb = new RadioButton(getContext());
                        rb.setText(val);
                        rg.addView(rb);
                    }
                    layout.addView(rg);

                    rg.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton selected = group.findViewById(checkedId);
                        if (selected != null) {
                            selectedOptions.put(option.getName(), selected.getText().toString());
                        }
                    });
                    break;

                case TEXT:
                    EditText et = new EditText(getContext());
                    et.setHint("Nhập " + option.getName().toLowerCase());
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    layout.addView(et);

                    et.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            selectedOptions.put(option.getName(), et.getText().toString());
                        }
                    });
                    break;
            }
        }

        // Số lượng
        LinearLayout quantityLayout = new LinearLayout(getContext());
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
        quantityLayout.setPadding(0, 48, 0, 32);

        Button btnMinus = new Button(getContext());
        btnMinus.setText("-");
        TextView tvQuantity = new TextView(getContext());
        tvQuantity.setText(String.valueOf(quantity));
        tvQuantity.setPadding(32, 0, 32, 0);
        Button btnPlus = new Button(getContext());
        btnPlus.setText("+");

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        quantityLayout.addView(btnMinus);
        quantityLayout.addView(tvQuantity);
        quantityLayout.addView(btnPlus);
        layout.addView(quantityLayout);

        // Button xác nhận
        Button btnAdd = new Button(getContext());
        btnAdd.setText("Thêm vào giỏ hàng");
        btnAdd.setOnClickListener(v -> {
            StringBuilder result = new StringBuilder("Sản phẩm: " + productName + "\n");

            for (ProductOption o : options) {
                String val = selectedOptions.getOrDefault(o.getName(), "Chưa chọn");
                result.append(o.getName()).append(": ").append(val).append("\n");
            }

            result.append("Số lượng: ").append(quantity);
            Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();
            dismiss();
        });

        layout.addView(btnAdd);
        return scrollView;
    }
}