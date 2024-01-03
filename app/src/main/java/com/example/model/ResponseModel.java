package com.example.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;


public class ResponseModel<T> {
    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    // 此处使用Object类型以便接收任何类型的data字段
    @SerializedName("data")
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}