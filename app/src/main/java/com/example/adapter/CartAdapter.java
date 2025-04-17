package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appclient.R;
import com.example.model.cart.CartItem;
import com.example.model.product.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> products;
    private OnQuantityChangeListener quantityChangeListener;
    private OnRemoveItemListener removeItemListener;

    public CartAdapter(List<CartItem> products,OnQuantityChangeListener listener,OnRemoveItemListener removeListener) {
        this.products = products;
        this.quantityChangeListener = listener;
        this.removeItemListener = removeListener;
    }
    public interface OnQuantityChangeListener {
        void onQuantityChanged(CartItem product, int newQuantity);
    }
    public interface OnRemoveItemListener {
        void onRemoveItem(CartItem product);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = products.get(position);
        holder.bind(cartItem, quantityChangeListener,removeItemListener);

    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvPrice;
        private  TextView tvQuantity;
        private View btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }

        public void bind(CartItem cartItem, OnQuantityChangeListener listener,OnRemoveItemListener removeListener) {
            Product product = cartItem.getProduct();
            tvName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText(String.format("$%d", product.getPrice()));
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            btnIncrease.setOnClickListener(v -> {
                int newQuantity = cartItem.getQuantity() + 1;
                listener.onQuantityChanged(cartItem, newQuantity);
            });

            btnDecrease.setOnClickListener(v -> {
                int newQuantity = cartItem.getQuantity() - 1;
                if (newQuantity >= 0) {
                    listener.onQuantityChanged(cartItem, newQuantity);
                }
            });
        }
    }
}