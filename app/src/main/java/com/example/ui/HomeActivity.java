package com.example.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appclient.R;
import com.example.model.auth.UserResponse;
import com.example.network.RetrofitClient;
import com.example.ui.Fragment.HomeFragment;
import com.example.ui.Fragment.ProductFragment;
import com.example.ui.Fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView ivCart;
    private TextView tvUserName, tvUserEmail;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang CartActivity
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });
        loadFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
                Toast.makeText(HomeActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navigation_user) {
                selectedFragment = new UserFragment();
                Toast.makeText(HomeActivity.this, "User clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navigation_product) {
                selectedFragment = new ProductFragment();
                Toast.makeText(HomeActivity.this, "Product clicked", Toast.LENGTH_SHORT).show();
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;

        });
        loadUserInfo();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void loadUserInfo() {
        if (token == null) {
            Toast.makeText(this, "Không có token, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }
        // Nếu API của bạn yêu cầu "Bearer " trước token, hãy thêm vào
        String authToken = "Bearer " + token;
        Call<UserResponse> call = RetrofitClient.getApiService().getUserInfo(authToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    // Cập nhật UI với thông tin người dùng
                    tvUserName.setText(user.getName());
                    tvUserEmail.setText(user.getEmail());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có thông tin lỗi";
                        Log.e("HomeActivity", "Lấy thông tin người dùng thất bại. Code: " + response.code() + ", Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("HomeActivity", "Error parsing errorBody", e);
                    }
                    Toast.makeText(HomeActivity.this, "Lấy thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("HomeActivity", "Lỗi khi call API lấy thông tin người dùng", t);
                Toast.makeText(HomeActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}