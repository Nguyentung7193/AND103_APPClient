package com.example.network;

import com.example.model.auth.LoginRequest;
import com.example.model.auth.LoginResponse;
import com.example.model.auth.RegisterRequest;
import com.example.model.auth.RegisterResponse;
import com.example.model.auth.UserResponse;
import com.example.model.cart.AddToCartRequest;
import com.example.model.cart.Cart;
import com.example.model.cart.CartResponse;
import com.example.model.categories.Categories;
import com.example.model.product.Product;
import com.example.model.product.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("auth/getInforUser")
    Call<UserResponse> getUserInfo(@Header("Authorization") String token);

    @GET("products/type/{type}")
    Call<ProductResponse> getProductsByType(
            @Header("Authorization") String token,
            @Path("type") String type
    );
    @GET("cart")
    Call<ApiResponse<Cart>> getCart(@Header("Authorization") String token);

    @GET("categories")
    Call<ApiResponse<List<Categories>>> getCategory(@Header("Authorization") String token);

    @POST("cart/add")
    Call<CartResponse> addToCart(@Header("Authorization") String token, @Body AddToCartRequest request);



}