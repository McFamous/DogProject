package com.example.dogproject.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogproject.R;
import com.example.dogproject.data.DbHelper;
import com.example.dogproject.data.FavouriteDogs;
import com.example.dogproject.pojo.Dogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<String> images = new ArrayList<>();
    private ViewGroup parent;
    private String breed;
    private String subBreed;
    private String allImages = "";

    public ImageAdapter(String breed, String subBreed) {
        this.breed = breed;
        this.subBreed = subBreed;
    }

    public void setItems(ArrayList<String> imageURL) {
        clearItems();
        images.addAll(imageURL);
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    public void clearItems() {
        images.clear();
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Picasso.with(parent.getContext()).load(images.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private FloatingActionButton fab;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_dog);
            fab = itemView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String image = images.get(getLayoutPosition());
                    Toast.makeText(parent.getContext(), "Picture added", Toast.LENGTH_SHORT).show();
                    initAllImages();
                    insertFavourite(allImages, image);
                }
            });
        }
        private void initAllImages(){
            DbHelper mDbHelper = new DbHelper(parent.getContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    FavouriteDogs.FavouriteImages._ID,
                    FavouriteDogs.FavouriteImages.COLUMN_NAME,
                    FavouriteDogs.FavouriteImages.COLUMN_IMAGES};

            Cursor cursor = db.query(
                    FavouriteDogs.FavouriteImages.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            int nameColumnIndex = cursor.getColumnIndex(FavouriteDogs.FavouriteImages.COLUMN_NAME);
            int imageColumnIndex = cursor.getColumnIndex(FavouriteDogs.FavouriteImages.COLUMN_IMAGES);

            while (cursor.moveToNext()){
                String currentBreed = cursor.getString(nameColumnIndex);
                String currentImages = cursor.getString(imageColumnIndex);
                if (currentBreed.equals(breed) && breed.length() != 0)
                    allImages = currentImages;
                else if(currentBreed.equals(subBreed) && subBreed.length() != 0)
                    allImages = currentImages;
            }
        }
        private void insertFavourite(String allImages, String image){
            DbHelper mDbHelper = new DbHelper(parent.getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            if(allImages.length() != 0){
                if(!allImages.contains(image)){
                    allImages += image + ", ";
                    values.put(FavouriteDogs.FavouriteImages.COLUMN_IMAGES, allImages);
                    if(subBreed.length() == 0)
                        db.update(FavouriteDogs.FavouriteImages.TABLE_NAME, values,
                                FavouriteDogs.FavouriteImages.COLUMN_NAME + "= ?", new String[]{breed});
                    else
                        db.update(FavouriteDogs.FavouriteImages.TABLE_NAME, values,
                                FavouriteDogs.FavouriteImages.COLUMN_NAME + "= ?", new String[]{subBreed});
                }
            }
            else{
                allImages += image + ", ";
                if(subBreed.length() == 0)
                    values.put(FavouriteDogs.FavouriteImages.COLUMN_NAME, breed);
                else
                    values.put(FavouriteDogs.FavouriteImages.COLUMN_NAME, subBreed);
                values.put(FavouriteDogs.FavouriteImages.COLUMN_IMAGES, allImages);
                db.insert(FavouriteDogs.FavouriteImages.TABLE_NAME, null, values);
            }
        }
    }
}
