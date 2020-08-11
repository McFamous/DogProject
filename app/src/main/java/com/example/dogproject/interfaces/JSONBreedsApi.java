package com.example.dogproject.interfaces;

import com.example.dogproject.pojo.Value;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONBreedsApi {
    @GET("/api/breeds/list/all")
    Call<Value> getValue();
}
