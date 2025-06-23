package com.example.thecoffeehouse.popup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.activity.LoginActivity;
import com.example.thecoffeehouse.model.ProductOption;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductOptionBottomSheet extends BottomSheetDialogFragment {
    private final String productName;
    private final List<ProductOption> options;
    private final OnAddToCartCompleteListener addToCartCompleteListener;
    private SharedPreferences mPreferences;

    public interface OnAddToCartCompleteListener {
        void onAdded(int quantity, Map<String, String> selectedOptions);
    }

    public ProductOptionBottomSheet(String productName, List<ProductOption> options,
                                    OnAddToCartCompleteListener listener) {
        this.productName = productName;
        this.options = options;
        this.addToCartCompleteListener = listener;
    }

    private int quantity = 1;
    private final Map<String, String> selectedOptions = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_product_option_full, container, false);
        LinearLayout layout = view.findViewById(R.id.layout_options);
        mPreferences = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        layout.addView(createTitle("Tùy chọn cho: " + productName));

        for (ProductOption option : options) {
            layout.addView(createTitle(option.getName()));

            if (option.getType() == ProductOption.OptionType.RADIO) {
                layout.addView(createRadioGroup(option));
            } else if (option.getType() == ProductOption.OptionType.TEXT) {
                layout.addView(createTextInput(option));
            }
        }

        layout.addView(createQuantityView());
        layout.addView(createAddToCartButton());

        return view;
    }

    private View createTitle(String titleText) {
        TextView title = new TextView(getContext());
        title.setText(titleText);
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.BLACK);
        title.setPadding(0, 0, 0, 24);
        return title;
    }

    private View createRadioGroup(ProductOption option) {
        RadioGroup rg = new RadioGroup(getContext());
        rg.setOrientation(RadioGroup.HORIZONTAL);
        rg.setPadding(0, 8, 0, 24);

        for (int i = 0; i < option.getValues().size(); i++) {
            String val = option.getValues().get(i);
            RadioButton rb = new RadioButton(getContext());
            rb.setText(val);
            rb.setTextSize(15);
            rb.setTextColor(Color.DKGRAY);
            rb.setId(View.generateViewId());
            rg.addView(rb);

            if (i == 0) {
                rb.setChecked(true);
                selectedOptions.put(option.getName(), val);
            }
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = group.findViewById(checkedId);
            if (selected != null) {
                selectedOptions.put(option.getName(), selected.getText().toString());
            }
        });

        return rg;
    }

    private View createTextInput(ProductOption option) {
        EditText et = new EditText(getContext());
        et.setHint("Nhập " + option.getName().toLowerCase());
        et.setBackgroundResource(R.drawable.bg_bottom_sheet);
        et.setPadding(24, 16, 24, 16);
        et.setTextSize(14);
        et.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                selectedOptions.put(option.getName(), et.getText().toString());
            }
        });

        return et;
    }

    private View createQuantityView() {
        LinearLayout quantityLayout = new LinearLayout(getContext());
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
        quantityLayout.setGravity(Gravity.CENTER_VERTICAL);
        quantityLayout.setPadding(0, 24, 0, 24);
        quantityLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageButton btnMinus = new ImageButton(getContext());
        btnMinus.setImageResource(R.drawable.remove);
        btnMinus.setBackgroundResource(R.drawable.bg_circle);
        btnMinus.setPadding(24, 24, 24, 24);

        TextView tvQuantity = new TextView(getContext());
        tvQuantity.setText(String.valueOf(quantity));
        tvQuantity.setTextSize(18);
        tvQuantity.setTypeface(null, Typeface.BOLD);
        tvQuantity.setPadding(32, 0, 32, 0);

        ImageButton btnPlus = new ImageButton(getContext());
        btnPlus.setImageResource(R.drawable.ic_add);
        btnPlus.setBackgroundResource(R.drawable.bg_circle);
        btnPlus.setPadding(24, 24, 24, 24);

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

        return quantityLayout;
    }

    private View createAddToCartButton() {
        Button btnAdd = new Button(getContext());
        btnAdd.setText("Thêm vào giỏ hàng");
        btnAdd.setTextColor(Color.WHITE);
        btnAdd.setTextSize(16);
        btnAdd.setPadding(0, 24, 0, 24);
        btnAdd.setBackgroundResource(R.drawable.bg_button_primary);

        btnAdd.setOnClickListener(v -> {
            if (mPreferences.getBoolean("isLoggedIn", false)) {
                if (addToCartCompleteListener != null) {
                    addToCartCompleteListener.onAdded(quantity, selectedOptions);
                }
                dismiss();
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return btnAdd;
    }
}
