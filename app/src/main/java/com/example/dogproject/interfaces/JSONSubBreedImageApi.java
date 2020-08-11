package com.example.dogproject.interfaces;

import com.example.dogproject.pojo.Images;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONSubBreedImageApi {
    @GET("/api/breed/{breed}/{subbreed}/images")
    Call<Images> getImages(@Path("breed") String breed, @Path("subbreed") String subbreed);
}
