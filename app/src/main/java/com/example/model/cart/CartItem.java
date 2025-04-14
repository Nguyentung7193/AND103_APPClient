package com.example.model.cart;

import com.example.model.product.Product;

public class CartItem {
    private Product product;
    private int quantity;
    private String _id;
    // 1. Constructor không tham số (nếu bạn cần dùng Gson/Retrofit deserialize)
    public CartItem() {}

    // 2. Constructor nhận Product và quantity
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getter & Setter
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
}

