package com.example.dogproject.activitys;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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

import com.example.dogproject.DialogShare;
import com.example.dogproject.R;
import com.example.dogproject.adapters.FavouriteImageAdapter;
import com.example.dogproject.data.FavouriteDogs;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    public static final String PICTURES = "pictures";
    public final static String FAVOURITE = "favourite";

    private androidx.appcompat.widget.Toolbar toolbarFavourite;
    private TextView titleFavourite;
    private ImageView backBreeds;
    private ImageView share;
    private TextView titleBack;

    private RecyclerView favouriteImages;
    private FavouriteImageAdapter adapter;
    private SnapHelper snapHelper;

    private ArrayList<String> images;
    private String strTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_image);

        toolbarFavourite = findViewById(R.id.toolbar_back_actionbar);
        titleFavourite = findViewById(R.id.toolbar_share_title);
        backBreeds = findViewById(R.id.image_share_back);
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = DialogShare.getDialog(FavouriteActivity.this);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
            }
        });
        titleBack = findViewById(R.id.title_share_back);
        backBreeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleBack.setText("Favourites");
        strTitle = getIntent().getStringExtra(PICTURES);
        titleFavourite.setText(strTitle.substring(0,1).toUpperCase() + strTitle.substring(1));
        setSupportActionBar(toolbarFavourite);

        images = (ArrayList<String>) getIntent().getSerializableExtra(FAVOURITE);

        favouriteImages = findViewById(R.id.favourite_image_list);
        favouriteImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(favouriteImages);

        adapter = new FavouriteImageAdapter();
        adapter.setItems(images);
        favouriteImages.setAdapter(adapter);
    }
}
