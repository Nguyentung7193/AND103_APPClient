package com.example.network;

public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;

    // Constructor mặc định
    public ApiResponse() {
    }

    // Getters và Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
