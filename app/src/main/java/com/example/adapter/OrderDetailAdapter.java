package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appclient.R;
import com.example.model.order.OrderItem;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private Context context;
    private List<OrderItem> itemList;

    public OrderDetailAdapter(Context context, List<OrderItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        OrderItem item = itemList.get(position);
        holder.tvProductName.setText(item.getName());
        holder.tvProductPrice.setText("Giá: " + item.getPrice() + "đ");
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvTotal.setText("Thành tiền: " + item.getTotal() + "đ");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvQuantity, tvTotal;

        public OrderDetailViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
