package com.example.thecoffeehouse.popup;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecoffeehouse.R;
import com.example.thecoffeehouse.activity.CheckoutActivity;
import com.example.thecoffeehouse.adapter.CartAdapter;
import com.example.thecoffeehouse.listener.CloseCartListener;
import com.example.thecoffeehouse.model.CartItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BottomSheetCart extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private MaterialButton btnCheckout;
    private TextView tvTotalPrice;


    private CloseCartListener listener;

    public BottomSheetCart(List<CartItem> items, CloseCartListener listener) {
        this.cartItems = items;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerCartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext(), cartItems, this::updateCart);
        recyclerView.setAdapter(adapter);

        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        updateCart();

        btnCheckout.setOnClickListener(v -> {

            Intent checkoutIntent = new Intent(getActivity(), CheckoutActivity.class);
            startActivity(checkoutIntent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteCartItem(cartItems.get(position));
                cartItems.remove(position);
                adapter.notifyItemRemoved(position);
                updateCart();
                listener.onchangeListCart();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#FFCDD2")); // màu nền xoá

                if (dX < 0) { // Kéo sang trái
                    c.drawRect(itemView.getRight() + dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom(), paint);

                    Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete);
                    if (deleteIcon != null) {
                        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + iconMargin;
                        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                        int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.draw(c);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void updateCart() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += (item.getQuantity() * item.getProductPrice());
            //TODO: Nhớ viết api nha Long
//            databaseHelper.updateCart(item.getId(), item.getProductId(), item.getQuantity(), item.getUserId());
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText("Tổng: " + formatter.format(total));
        if (total == 0) {
            btnCheckout.setVisibility(GONE);
        }

    }

    private void deleteCartItem(CartItem cartItem) {
        //TODO: Nhớ viết API nha long
//        databaseHelper.deleteCartItem(cartItem);
    }
}
