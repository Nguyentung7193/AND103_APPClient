package com.example.model.order;


public class CreateOrderRequest {
    private String fullname;
    private String address;
    private String phone;
    private String note;
    private String type;
    private String fcmToken;

    public CreateOrderRequest(String fullname, String address, String phone, String note, String type, String fcmToken) {
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.type = type;
        this.fcmToken = fcmToken;
    }

    // Getters và setters nếu cần
}
