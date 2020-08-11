package com.example.dogproject.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogproject.DialogScreen;
import com.example.dogproject.NetworkService;
import com.example.dogproject.R;
import com.example.dogproject.adapters.SubBreedAdapter;
import com.example.dogproject.pojo.SubBreed;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubBreedsActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbarSubBreed;
    private TextView titleSubBreed;
    private ImageView backBreeds;
    private TextView titleBack;

    private RecyclerView subBreeds;
    private SubBreedAdapter subBreedAdapter;

    public static final String DOG_BREED = "breed";
    private String breed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_breed);

        toolbarSubBreed = findViewById(R.id.toolbar_back_actionbar);
        titleSubBreed = findViewById(R.id.toolbar_back_title);
        backBreeds = findViewById(R.id.image_back);
        titleBack = findViewById(R.id.title_back);
        backBreeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        breed = getIntent().getStringExtra(DOG_BREED);
        titleBack.setText("Breeds");
        titleSubBreed.setText(breed);
        setSupportActionBar(toolbarSubBreed);

        initRecyclerView();
    }

    private void initRecyclerView() {
        subBreeds = findViewById(R.id.dogs_sub_breeds_list);
        subBreeds.setLayoutManager(new LinearLayoutManager(this));

        SubBreedAdapter.OnSubBreedClickListener onSubBreedClickListener = new SubBreedAdapter.OnSubBreedClickListener() {
            @Override
            public void onSubBreedClick(String subBreed) {
                Intent intent = new Intent(SubBreedsActivity.this, ImageActivity.class);
                intent.putExtra(ImageActivity.BREED, breed);
                intent.putExtra(ImageActivity.SUB_BREED, subBreed);
                startActivity(intent);
            }
        };

        subBreedAdapter = new SubBreedAdapter(onSubBreedClickListener);
        subBreeds.setAdapter(subBreedAdapter);

        NetworkService.getInstance()
                .getSubBreeds()
                .getValue(breed.toLowerCase())
                .enqueue(new Callback<SubBreed>() {
            @Override
            public void onResponse(Call<SubBreed> call, Response<SubBreed> response) {
                if(response.body().getStatus().equals("success"))
                    subBreedAdapter.setItems(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<SubBreed> call, Throwable t) {
                AlertDialog dialog = DialogScreen.getDialog(SubBreedsActivity.this);
                dialog.show();
            }
        });
    }
}
