package com.example.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.OrderAdapter;
import com.example.adapter.OrderDetailAdapter;
import com.example.appclient.R;
import com.example.model.cart.CartResponse;
import com.example.model.order.Order;
import com.example.model.product.Product;
import com.example.network.ApiService;
import com.example.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvFullname, tvAddress, tvPhone, tvTotalPrice, tvNote, tvType;
    private RecyclerView rvOrderItems;
    private OrderDetailAdapter orderAdapter;
    private List<Product> productList = new ArrayList<>();
    private ApiService apiService;
    private String authToken;
    private SharedPreferences sharedPreferences;
    private String orderId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);
        orderId = getIntent().getStringExtra("ORDER_ID");
        tvFullname = findViewById(R.id.tvFullnameOder);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvNote = findViewById(R.id.tvNote);
        tvType = findViewById(R.id.tvType);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderDetailAdapter(this, productList);
        rvOrderItems.setAdapter(orderAdapter);
        apiService = RetrofitClient.getApiService();
        fetchOrderDetails();
    }
    private void fetchOrderDetails() {
        apiService.getOrderById("Bearer " + authToken, orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = response.body();
                    // Cập nhật thông tin đơn hàng vào TextViews
                    tvFullname.setText(order.getFullname());
                    tvAddress.setText(order.getAddress());
                    tvPhone.setText(order.getPhone());
                    tvTotalPrice.setText("Tổng tiền: " + order.getTotalPrice());
                    tvNote.setText(order.getNote());
                    tvType.setText("Loại: " + order.getType());
                    productList.clear();
                    productList.addAll(order.getItems());
                    orderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi lấy chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}