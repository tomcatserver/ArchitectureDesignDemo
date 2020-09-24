package com.example.network.bean;

import com.google.gson.annotations.Expose;

public class Response<T> {
    @Expose
    private T data;

    public T getData() {
        return data;
    }
}
