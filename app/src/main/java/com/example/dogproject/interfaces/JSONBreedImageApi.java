package com.example.dogproject.interfaces;

import com.example.dogproject.pojo.Images;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONBreedImageApi {
    @GET("/api/breed/{breed}/images")
    Call<Images> getImages(@Path("breed") String breed);
}
