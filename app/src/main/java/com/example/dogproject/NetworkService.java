package com.example.dogproject;
import com.example.dogproject.interfaces.JSONBreedImageApi;
import com.example.dogproject.interfaces.JSONBreedsApi;
import com.example.dogproject.interfaces.JSONSubBreedImageApi;
import com.example.dogproject.interfaces.JSONSubBreedsApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService mInstance;
    private Retrofit mRetrofit;
    private static final String BASE_URL = "https://dog.ceo";


    public static NetworkService getInstance(){
        if(mInstance == null)
            mInstance = new NetworkService();
        return mInstance;
    }

    public NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public JSONBreedsApi getBreeds(){ return mRetrofit.create(JSONBreedsApi.class); }
    public JSONSubBreedsApi getSubBreeds(){
        return mRetrofit.create(JSONSubBreedsApi.class);
    }
    public JSONSubBreedImageApi getSubBreedImages(){ return mRetrofit.create(JSONSubBreedImageApi.class); }
    public JSONBreedImageApi getBreedImages(){ return mRetrofit.create(JSONBreedImageApi.class); }

}