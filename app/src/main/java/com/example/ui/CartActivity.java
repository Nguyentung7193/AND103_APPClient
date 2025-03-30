package com.example.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CartAdapter;
import com.example.appclient.R;
import com.example.model.cart.Cart;
import com.example.model.product.Product;
import com.example.network.ApiResponse;
import com.example.network.ApiService;
import com.example.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnPurchase;
    private RecyclerView rvCartProducts;
    private CartAdapter cartAdapter;
    private List<Product> productList = new ArrayList<>();
    private ApiService apiService;
    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        authToken = getIntent().getStringExtra("TOKEN");
        btnBack = findViewById(R.id.btnBack);
        btnPurchase = findViewById(R.id.btnPurchase);
        rvCartProducts = findViewById(R.id.rvCartProducts);
        apiService = RetrofitClient.getApiService();

        // Cài đặt RecyclerView
        rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(productList);
        rvCartProducts.setAdapter(cartAdapter);

        // Sự kiện cho nút quay lại: kết thúc Activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ví dụ: thông báo đặt mua thành công
                Toast.makeText(CartActivity.this, "Đặt mua thành công!", Toast.LENGTH_SHORT).show();
                // Ở đây bạn có thể gọi API đặt mua hoặc chuyển sang màn hình thanh toán
            }
        });
       fetchCart();
    }
    private void fetchCart() {
        apiService.getCart("Bearer " + authToken).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cart cart = response.body().getData();
                    List<Product> productList = new ArrayList<>();
                    if (cart != null && cart.getItems() != null) {
                        for (Cart.CartItem item : cart.getItems()) {
                            productList.add(item.getProduct());
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    }
                    // Cập nhật adapter với danh sách sản phẩm
                    cartAdapter = new CartAdapter(productList);
                    rvCartProducts.setAdapter(cartAdapter);
                } else {
                    Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi API: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }


}