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

public class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.BreedViewHolder>{

    private ArrayList<Dogs> listBreeds = new ArrayList<>();
    private OnBreedClickListener onBreedClickListener;

    public BreedAdapter(OnBreedClickListener onBreedClickListener) {
        this.onBreedClickListener = onBreedClickListener;
    }

    public void setItems(ArrayList<Dogs> newBreeds) {
        clearItems();
        listBreeds.addAll(newBreeds);
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    public void clearItems() {
        listBreeds.clear();
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }
    @Override
    public BreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_item_list, parent, false);
        return new BreedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedViewHolder holder, int position) {
        holder.breed.setText(listBreeds.get(position).getBreed());
        if(listBreeds.get(position).getCountSubBreed() != 0)
            holder.countSubBreed.setText("(" + listBreeds.get(position).getCountSubBreed() + " subbreeds)");
        else
            holder.countSubBreed.setText("");
    }

    @Override
    public int getItemCount() {
        return listBreeds.size();
    }

    class BreedViewHolder extends RecyclerView.ViewHolder {

        private TextView breed;
        private TextView countSubBreed;

        public BreedViewHolder(@NonNull View itemView) {
            super(itemView);
            breed = itemView.findViewById(R.id.dog_breed);
            countSubBreed = itemView.findViewById(R.id.count_sub_breed);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dogs dog = listBreeds.get(getLayoutPosition());
                    onBreedClickListener.onBreedClick(dog);
                }
            });
        }
    }

    public interface OnBreedClickListener{
        void onBreedClick(Dogs dog);
    }
}