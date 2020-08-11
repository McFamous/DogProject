package com.example.dogproject.interfaces;

import com.example.dogproject.pojo.SubBreed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONSubBreedsApi {
    @GET("/api/breed/{breed}/list")
    Call<SubBreed> getValue(@Path("breed") String breed);
}
