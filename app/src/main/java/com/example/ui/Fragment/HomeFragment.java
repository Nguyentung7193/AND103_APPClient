package com.example.ui.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adapter.CategoryAdapter;
import com.example.adapter.ProductAdapter;
import com.example.appclient.R;
import com.example.model.cart.AddToCartRequest;
import com.example.model.cart.CartResponse;
import com.example.model.categories.Categories;
import com.example.model.product.Product;
import com.example.model.product.ProductResponse;
import com.example.network.ApiResponse;
import com.example.network.RetrofitClient;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private RecyclerView rvCategories;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private String productType = "Tất cả";

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvProducts = view.findViewById(R.id.rvProductsHome);
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter();
        rvProducts.setAdapter(productAdapter);
//        loadProductsByType(productType);

        rvCategories = view.findViewById(R.id.rvCategories);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(horizontalLayoutManager);
        categoryAdapter = new CategoryAdapter();
        rvCategories.setAdapter(categoryAdapter);
        loadCategories(); // Tải danh sách danh mục
        loadProductsByType("all"); // Mặc định tải tất cả sản phẩm
        categoryAdapter.setOnItemClickListener((category, position) -> {
            String selectedType = category.getName();
            if(selectedType.equalsIgnoreCase("Tất cả")){
                // Nếu chọn "Tất cả" thì gọi API không truyền type
                loadProductsByType("all");
            } else {
                loadProductsByType(selectedType);
            }
        });
        productAdapter.setOnAddToCartClickListener(product -> {
            addToCart(product.get_id(), 1); // Giả sử thêm 1 sản phẩm
        });
        return view;
    }
    private void loadCategories() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }
        String authToken = "Bearer " + token;
        // Gọi API lấy danh sách category
        Call<ApiResponse<List<Categories>>> call = RetrofitClient.getApiService().getCategory(authToken);
        call.enqueue(new Callback<ApiResponse<List<Categories>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Categories>>> call, Response<ApiResponse<List<Categories>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categories> categories = response.body().getData();
                    Categories allCategory = new Categories();
                    allCategory.setName("Tất cả");
                    categories.add(0, allCategory);
                    categoryAdapter.setCategories(categories);
                } else {
                    Toast.makeText(getContext(), "Lấy danh sách category thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("HomeFragment", "Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Categories>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "onFailure: ", t);
            }
        });
    }
    private void loadProductsByType(String type) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu API yêu cầu "Bearer " trước token, thêm vào
        String authToken = "Bearer " + token;
        Call<ProductResponse> call = RetrofitClient.getApiService().getProductsByType(authToken,type);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    List<Product> products = productResponse.getData();
                    productAdapter.setProducts(products);
                } else {
                    Toast.makeText(getContext(), "Lấy danh sách sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("HomeFragment", "Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "onFailure: ", t);
            }
        });
    }
    private void addToCart(String productId, int quantity) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token == null) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }
        String authToken = "Bearer " + token;
        AddToCartRequest request = new AddToCartRequest(productId, quantity);
        Call<CartResponse> call = RetrofitClient.getApiService().addToCart(authToken, request);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm sản phẩm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("HomeFragment", "Add to Cart Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Add to Cart onFailure: ", t);
            }
        });
    }
}