package com.example.model.auth;

public class RegisterResponse {
    private int userId;
    private String message;

    // Getter và Setter
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
