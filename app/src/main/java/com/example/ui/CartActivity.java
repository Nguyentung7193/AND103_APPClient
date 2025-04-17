package com.example.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.model.cart.CartItem;
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
    private List<CartItem> productList = new ArrayList<>();
    private ApiService apiService;
    String authToken;
    private SharedPreferences sharedPreferences;
    String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        authToken = getIntent().getStringExtra("TOKEN");
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        fcmToken = sharedPreferences.getString("fcm_token", null);
        btnBack = findViewById(R.id.btnBack);
        btnPurchase = findViewById(R.id.btnPurchase);
        rvCartProducts = findViewById(R.id.rvCartProducts);
        apiService = RetrofitClient.getApiService();

        // Cài đặt RecyclerView
        rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(productList, (cartItem, newQuantity) -> {
            updateCartItem(cartItem.getProduct().get_id(), newQuantity);
            cartItem.setQuantity(newQuantity);
            cartAdapter.notifyDataSetChanged();
        },cartItem -> {
            removeCartItem(cartItem.getProduct().get_id());
        });
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
                showOrderDialog();;
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
                    List<CartItem> cartItems = new ArrayList<>();
                    if (cart != null && cart.getItems() != null) {
                        for (Cart.CartItem item : cart.getItems()) {
                            cartItems.add(new CartItem(item.getProduct(), item.getQuantity()));
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    }

                    cartAdapter = new CartAdapter(cartItems, (cartItem, newQuantity) -> {
                        updateCartItem(cartItem.getProduct().get_id(), newQuantity);
                        cartItem.setQuantity(newQuantity);
                        cartAdapter.notifyDataSetChanged();
                    },cartItem -> {
                        removeCartItem(cartItem.getProduct().get_id());
                    });

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
    private void updateCartItem(String productId, int newQuantity) {
        apiService.updateCartItem("Bearer " + authToken, productId, newQuantity)
                .enqueue(new Callback<ApiResponse<Cart>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CartActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CartActivity.this, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                        Toast.makeText(CartActivity.this, "Lỗi server", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void removeCartItem(String productId) {
        apiService.removeCartItem("Bearer " + authToken, productId)
                .enqueue(new Callback<ApiResponse<Cart>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật giỏ hàng sau khi xoá sản phẩm
                            fetchCart(); // Tải lại giỏ hàng
                            Toast.makeText(CartActivity.this, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CartActivity.this, "Lỗi khi xoá sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                        Toast.makeText(CartActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showOrderDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order_info, null);

        EditText etFullname = dialogView.findViewById(R.id.etFullname);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        EditText etType = dialogView.findViewById(R.id.etType);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Nhập thông tin đặt hàng")
                .setView(dialogView)
                .setPositiveButton("Đặt hàng", (dialog, which) -> {
                    String fullname = etFullname.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String note = etNote.getText().toString().trim();
                    String type = etType.getText().toString().trim();

                    if (fullname.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    createOrderFromCart(fullname, address, phone, note, type,fcmToken);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void createOrderFromCart(String fullname, String address, String phone, String note, String type, String fcmToken) {
        CreateOrderRequest orderRequest = new CreateOrderRequest(fullname, address, phone, note, type,fcmToken);
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