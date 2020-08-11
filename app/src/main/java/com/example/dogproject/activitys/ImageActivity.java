package com.example.dogproject.activitys;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.dogproject.DialogScreen;
import com.example.dogproject.DialogShare;
import com.example.dogproject.NetworkService;
import com.example.dogproject.R;
import com.example.dogproject.adapters.ImageAdapter;
import com.example.dogproject.pojo.Images;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbarImage;
    private TextView titleImage;
    private ImageView backBreeds;
    private ImageView share;

    public static final String BREED = "BREED";
    public static final String SUB_BREED = "SUB_BREED";

    private String breed;
    private String subBreed;
    private RecyclerView dogPhotos;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dog);

        toolbarImage = findViewById(R.id.toolbar_images_actionbar);
        titleImage = findViewById(R.id.toolbar_share_title);
        backBreeds = findViewById(R.id.image_share_back);
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = DialogShare.getDialog(ImageActivity.this);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();

            }
        });
        backBreeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbarImage);

        breed = getIntent().getStringExtra(BREED);
        subBreed = getIntent().getStringExtra(SUB_BREED);
        dogPhotos = findViewById(R.id.image_list);

        initRecyclerView();
    }

    private void initRecyclerView() {
        SnapHelper snapHelper = new PagerSnapHelper();

        dogPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(dogPhotos);
        adapter = new ImageAdapter(breed, subBreed);
        dogPhotos.setAdapter(adapter);

        if(subBreed.length() == 0)
            initImageBreed();
        else
            initImageSubBreed();
    }

    private void initImageBreed() {
        titleImage.setText(breed);
        NetworkService.getInstance()
                .getBreedImages()
                .getImages(breed.toLowerCase())
                .enqueue(new Callback<Images>() {
                    @Override
                    public void onResponse(Call<Images> call, Response<Images> response) {
                        if(response.body().getStatus().equals("success"))
                            adapter.setItems(response.body().getMessage());
                    }
                    @Override
                    public void onFailure(Call<Images> call, Throwable t) {
                        AlertDialog dialog = DialogScreen.getDialog(ImageActivity.this);
                        dialog.show();
                    }
                });
    }

    private void initImageSubBreed() {
        titleImage.setText(subBreed.substring(0,1).toUpperCase() + subBreed.substring(1));
        NetworkService.getInstance()
                .getSubBreedImages()
                .getImages(breed.toLowerCase(),subBreed)
                .enqueue(new Callback<Images>() {
                    @Override
                    public void onResponse(Call<Images> call, Response<Images> response) {
                        if(response.body().getStatus().equals("success"))
                            adapter.setItems(response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<Images> call, Throwable t) {
                        AlertDialog dialog = DialogScreen.getDialog(ImageActivity.this);
                        dialog.show();
                    }
                });
    }
}
