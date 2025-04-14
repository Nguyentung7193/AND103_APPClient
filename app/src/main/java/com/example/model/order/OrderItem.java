package com.example.model.order;

import com.example.model.product.Product;
import java.io.Serializable;

public class OrderItem implements Serializable {
    private Product product; // có thể là null nếu không populate từ server
    private String name;
    private int price;
    private int quantity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name != null ? name : (product != null ? product.getName() : "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price != 0 ? price : (product != null ? product.getPrice() : 0);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return getPrice() * getQuantity();
    }
}
