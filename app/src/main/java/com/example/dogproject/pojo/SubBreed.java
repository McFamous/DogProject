package com.example.dogproject.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class SubBreed {
    @SerializedName("message")
    @Expose
    private ArrayList<String> message = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
