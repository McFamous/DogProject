package com.example.dogproject.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteImageAdapter extends RecyclerView.Adapter<FavouriteImageAdapter.FavouriteImageViewHolder> {

    private ArrayList<String> favouriteImageList = new ArrayList<>();
    private ViewGroup parent;
    private String breed = "";
    private String allImages = "";

    public void setItems(ArrayList<String> imageUrlList) {
        clearItems();
        favouriteImageList.addAll(imageUrlList);
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    public void clearItems() {
        favouriteImageList.clear();
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    @NonNull
    @Override
    public FavouriteImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_image_item_list, parent, false);
        return new FavouriteImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteImageViewHolder holder, int position) {
        Picasso.with(parent.getContext()).load(favouriteImageList.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return favouriteImageList.size();
    }

    class FavouriteImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private FloatingActionButton fab;

        public FavouriteImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.favourite_image_dog);
            fab = itemView.findViewById(R.id.delete_fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String imageURL = favouriteImageList.get(getLayoutPosition());
                    Toast.makeText(parent.getContext(), "Picture deleted", Toast.LENGTH_SHORT).show();
                    initAllImages(imageURL);
                    insertFavourite(allImages, imageURL);
                }
            });
        }

        private void initAllImages(String imageURL){
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
                if (currentImages.contains(imageURL)){
                    breed = currentBreed;
                    allImages = currentImages;
                }
            }
        }
        private void insertFavourite(String allImages, String imageURL){
            DbHelper mDbHelper = new DbHelper(parent.getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            allImages = allImages.replace(imageURL + ", ", "");
            if(allImages.length() != 0){
                values.put(FavouriteDogs.FavouriteImages.COLUMN_IMAGES, allImages);
                db.update(FavouriteDogs.FavouriteImages.TABLE_NAME, values,
                            FavouriteDogs.FavouriteImages.COLUMN_NAME + " =?",
                            new String[]{breed});
                Log.i("DBHelper", "good = " + values);
            }
            else
                db.delete(FavouriteDogs.FavouriteImages.TABLE_NAME,
                        FavouriteDogs.FavouriteImages.COLUMN_NAME + " =?", new String[]{breed});
        }
    }
}
