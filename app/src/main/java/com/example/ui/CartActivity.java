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
import com.example.model.order.CreateOrderRequest;
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrderFromCart();
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
    private void createOrderFromCart() {
        // Giả lập thông tin đơn hàng (sau này có thể cho người dùng nhập)
        String fullname = "Nguyễn Văn A";
        String address = "123 Đường ABC, TP.HCM";
        String phone = "0987654321";
        String note = "Giao buổi sáng";
        String type = "online";

        CreateOrderRequest orderRequest = new CreateOrderRequest(fullname, address, phone, note, type);

        apiService.createOrderFromCart("Bearer " + authToken, orderRequest)
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CartActivity.this, "Lỗi khi đặt hàng!", Toast.LENGTH_SHORT).show();
                            Log.e("ORDER_API", "Lỗi response: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(CartActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                        Log.e("ORDER_API", "Lỗi gọi API: " + t.getMessage());
                    }
                });
    }


}