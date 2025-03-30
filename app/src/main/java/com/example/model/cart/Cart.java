package com.example.model.cart;
import com.example.model.product.Product;

import java.util.List;

public class Cart {
    private String userID;
    private List<CartItem> items;

    public String getUserID() {
        return userID;
    }

    public List<CartItem> getItems() {
        return items;
    }
    public static class CartItem {
        private Product product;
        private int quantity;

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
