package com.example.dogproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogproject.R;
import com.example.dogproject.pojo.Dogs;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private ArrayList<Dogs> favouriteBreeds = new ArrayList<>();
    private OnFavouriteClickListener onFavouriteClickListener;

    public FavouriteAdapter(OnFavouriteClickListener onFavouriteClickListener) {
        this.onFavouriteClickListener = onFavouriteClickListener;
    }

    public void setItems(ArrayList<Dogs> newBreeds) {
        clearItems();
        favouriteBreeds.addAll(newBreeds);
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    public void clearItems() {
        favouriteBreeds.clear();
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_item_list, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        String fBreed = favouriteBreeds.get(position).getBreed();
        holder.breed.setText(fBreed.substring(0,1).toUpperCase() + fBreed.substring(1));
        holder.countPhoto.setText("(" + favouriteBreeds.get(position).getCountSubBreed() + " photo)");
    }

    @Override
    public int getItemCount() {
        return favouriteBreeds.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder{

        private TextView breed;
        private TextView countPhoto;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            breed = itemView.findViewById(R.id.dog_breed);
            countPhoto = itemView.findViewById(R.id.count_sub_breed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> imageDogs = favouriteBreeds.get(getLayoutPosition()).getSubBreed();
                    String breed = favouriteBreeds.get(getLayoutPosition()).getBreed();
                    onFavouriteClickListener.onFavouriteBreedClick(breed, imageDogs);
                }
            });
        }
    }
    public interface OnFavouriteClickListener{
        void onFavouriteBreedClick(String breed, ArrayList<String> imageDogs);
    }
}
