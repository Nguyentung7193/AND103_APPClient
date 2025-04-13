package com.example.ui.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adapter.OrderAdapter;
import com.example.appclient.R;
import com.example.model.order.Order;
import com.example.network.ApiService;
import com.example.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OderFragment extends Fragment {
    private RecyclerView rvOrders;
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private ApiService apiService;
    private String authToken;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oder, container, false);
        rvOrders = view.findViewById(R.id.rvOrders);
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("token", null);
        apiService = RetrofitClient.getApiService();

        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(getContext(), orderList);
        rvOrders.setAdapter(orderAdapter);

        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        apiService.getOrders("Bearer " + authToken).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}