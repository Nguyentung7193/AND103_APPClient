package com.example.network;

import com.example.model.auth.LoginRequest;
import com.example.model.auth.LoginResponse;
import com.example.model.auth.RegisterRequest;
import com.example.model.auth.RegisterResponse;
import com.example.model.auth.UserResponse;
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

}