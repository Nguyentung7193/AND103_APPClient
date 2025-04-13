package com.example.ui.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appclient.R;
import com.example.model.user.UserResponse;
import com.example.network.ApiResponse;
import com.example.network.RetrofitClient;
import com.example.ui.HomeActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private TextView tvName, tvEmail;
    private Button btnEdit;
    private String token;
    private SharedPreferences sharedPreferences;
    private UserResponse currentUser; // lưu dữ liệu user hiện tại
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> showEditDialog());
        loadUserInfo();
        return view;
    }
    private void loadUserInfo() {
        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        String authToken = "Bearer " + token;
        Call<ApiResponse<UserResponse>> call = RetrofitClient.getApiService().getUserInfo(authToken);

        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    UserResponse user = response.body().getData();
                    currentUser = response.body().getData();
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                } else {
                    String message = "Không lấy được thông tin người dùng";
                    if (response.body() != null) {
                        message = response.body().getMsg();
                    }
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    Log.e("HomeActivity", "Code: " + response.code() + ", Msg: " + message);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                Log.e("HomeActivity", "Lỗi khi call API lấy thông tin người dùng", t);
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("MissingInflatedId")
    private void showEditDialog() {
        if (currentUser == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_user, null);
         EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);

        edtName.setText(currentUser.getName());
        edtEmail.setText(currentUser.getEmail());

        new AlertDialog.Builder(getContext())
                .setTitle("Chỉnh sửa thông tin")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = edtName.getText().toString().trim();
                    String newEmail = edtEmail.getText().toString().trim();
                    updateUserInfo(newName, newEmail);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void updateUserInfo(String name, String email) {
        String authToken = "Bearer " + token;

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("email", email);

        Call<ApiResponse<UserResponse>> call = RetrofitClient.getApiService().updateUser(updates, authToken);

        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadUserInfo(); // reload lại thông tin
                } else {
                    String message = response.body() != null ? response.body().getMsg() : "Lỗi khi cập nhật";
                    Log.e("UpdateUser", "Error: " + message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                Log.e("UpdateUser", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}