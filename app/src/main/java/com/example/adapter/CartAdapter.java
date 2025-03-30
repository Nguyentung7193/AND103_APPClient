package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appclient.R;
import com.example.model.product.Product;

import java.util.List;

// Lưu ý: ở đây Product có thể là lớp riêng hoặc bạn dùng lớp inner từ CartActivity.
// Nếu muốn tách Product ra file riêng, bạn có thể tạo thêm file Product.java.
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> products;

    public CartAdapter(List<Product> products) {
        this.products = products;
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
        Product product = products.get(position);
        holder.bind(product);
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        public void bind(Product product) {
            tvName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText(String.format("$%d", product.getPrice()));
        }
    }
}