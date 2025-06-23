package com.example.thecoffeehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.listener.OnAddToCartListener;
import com.example.thecoffeehouse.model.Product;
import com.example.thecoffeehouse.model.ProductOption;
import com.example.thecoffeehouse.popup.ProductOptionBottomSheet;

import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {



    private final List<Product> productList;
    private final Context context;
    private final OnAddToCartListener addToCartListener;

    public ProductAdapter(Context context, List<Product> productList, OnAddToCartListener addToCartListener) {
        this.context = context;
        this.productList = productList;
        this.addToCartListener = addToCartListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()));
        com.bumptech.glide.Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.bg_count_badge)
                .into(holder.imgProduct);


        List<ProductOption> optionList = Arrays.asList(
                new ProductOption("Size", ProductOption.OptionType.RADIO, Arrays.asList("S", "M", "L")),
                new ProductOption("Mức đá", ProductOption.OptionType.RADIO, Arrays.asList("0%", "50%", "100%")),
                new ProductOption("Mức đường", ProductOption.OptionType.RADIO, Arrays.asList("0%", "50%", "100%")),
                new ProductOption("Ghi chú", ProductOption.OptionType.TEXT, null)
        );

        View.OnClickListener showOptionPopup = v -> {
            ProductOptionBottomSheet sheet = new ProductOptionBottomSheet(
                    product.getName(),
                    optionList,
                    (quantity, selectedOptions) -> {
                        if (addToCartListener != null) {
                            addToCartListener.onAddToCart(product, quantity);
                        }
                    }
            );
            sheet.show(((FragmentActivity) context).getSupportFragmentManager(), "ProductOptions");
        };

        holder.itemView.setOnClickListener(showOptionPopup);

        holder.btnAdd.setOnClickListener(showOptionPopup);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice;
        ImageButton btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
