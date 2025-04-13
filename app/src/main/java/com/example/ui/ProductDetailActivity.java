package com.example.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appclient.R;
import com.example.model.product.Product;
import com.example.network.ApiResponse;
import com.example.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {


    private TextView tvName, tvPrice, tvDescription;
    private ImageView imgProduct;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        imgProduct = findViewById(R.id.imgProduct);

        String productId = getIntent().getStringExtra("productId");
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (productId != null && token != null) {
            fetchProductDetail(token, productId);
        }
    }
    private void fetchProductDetail(String token, String productId) {
        String authToken = "Bearer " + token;
        Call<ApiResponse<Product>> call = RetrofitClient.getApiService().getProductById(authToken, productId);

        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body().getData();
                    tvName.setText(product.getName());
                    tvPrice.setText("Giá: " + product.getPrice());
                    tvDescription.setText(product.getDescription());
//                    Glide.with(ProductDetailActivity.this)
//                            .load(product.getImage()) // Đường dẫn ảnh từ API
//                            .placeholder(R.drawable.ic_placeholder)
//                            .error(R.drawable.ic_error)
//                            .into(imgProduct);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}