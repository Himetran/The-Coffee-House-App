package com.example.thecoffeehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.model.ProductModel;
import com.example.thecoffeehouse.model.ProductOption;
import com.example.thecoffeehouse.popup.ProductOptionBottomSheet;

import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<ProductModel> productList;
    private final Context context;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(product.getPrice());
        holder.imgProduct.setImageResource(product.getImageResId());

        List<ProductOption> optionList = Arrays.asList(
                new ProductOption("Size", ProductOption.OptionType.RADIO, Arrays.asList("S", "M", "L")),
                new ProductOption("Mức đá", ProductOption.OptionType.RADIO, Arrays.asList("0%", "50%", "100%")),
                new ProductOption("Mức đường", ProductOption.OptionType.RADIO, Arrays.asList("0%", "50%", "100%")),
                new ProductOption("Ghi chú", ProductOption.OptionType.TEXT, null)
        );
        holder.btnAdd.setOnClickListener(v -> {
            ProductOptionBottomSheet sheet = new ProductOptionBottomSheet(product.getName(), optionList);
            sheet.show(((FragmentActivity) context).getSupportFragmentManager(), "ProductOptions");
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
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
