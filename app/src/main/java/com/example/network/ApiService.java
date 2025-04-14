package com.example.network;

import com.example.model.auth.LoginRequest;
import com.example.model.auth.LoginResponse;
import com.example.model.auth.RegisterRequest;
import com.example.model.auth.RegisterResponse;
import com.example.model.order.CreateOrderRequest;
import com.example.model.order.Order;
import com.example.model.order.OrderResponse;
import com.example.model.user.UserResponse;
import com.example.model.cart.AddToCartRequest;
import com.example.model.cart.Cart;
import com.example.model.cart.CartResponse;
import com.example.model.categories.Categories;
import com.example.model.product.Product;
import com.example.model.product.ProductResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("auth/getInforUser")
    Call<ApiResponse<UserResponse>> getUserInfo(@Header("Authorization") String token);

    @PUT("auth/updateUser")
    Call<ApiResponse<UserResponse>> updateUser(
            @Body Map<String, Object> updates,
            @Header("Authorization") String token
    );

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

    @GET("products/detail/{id}")
    Call<ApiResponse<Product>> getProductById(
            @Header("Authorization") String token,
            @Path("id") String productId
    );

    @GET("products/search")
    Call<ApiResponse<List<Product>>> searchProducts(
            @Header("Authorization") String token,
            @Query("name") String keyword
    );

    @POST("orders/order/create")
    Call<ApiResponse<Object>> createOrderFromCart(
            @Header("Authorization") String token,
            @Body CreateOrderRequest request
    );
    @GET("orders/order")
    Call<List<Order>> getOrders(@Header("Authorization") String authToken);

    @GET("orders/order/{id}")
    Call<OrderResponse> getOrderById(@Header("Authorization") String authToken, @Path("id") String orderId);

    @PUT("cart/update")
    Call<ApiResponse<Cart>> updateCartItem(
            @Header("Authorization") String authToken,
            @Query("productId") String productId,
            @Query("quantity") int quantity
    );
    @DELETE("cart/remove/{productId}")
    Call<ApiResponse<Cart>> removeCartItem(
            @Header("Authorization") String token,
            @Path("productId") String productId
    );




}