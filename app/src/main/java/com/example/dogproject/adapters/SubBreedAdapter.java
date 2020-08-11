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

public class SubBreedAdapter extends RecyclerView.Adapter<SubBreedAdapter.SubBreedViewHolder> {

    private ArrayList<String> subBreedsList = new ArrayList<>();
    private OnSubBreedClickListener onSubBreedClickListener;


    public SubBreedAdapter(OnSubBreedClickListener onSubBreedClickListener) {
        this.onSubBreedClickListener = onSubBreedClickListener;
    }

    public void setItems(ArrayList<String> subBreeds) {
        clearItems();
        subBreedsList.addAll(subBreeds);
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    public void clearItems() {
        subBreedsList.clear();
        notifyDataSetChanged(); //для то чтобы адаптер понял, что пора перерисовать элементы на экране
    }

    @NonNull
    @Override
    public SubBreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_breed_item_list,parent,false);
        return new SubBreedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubBreedViewHolder holder, int position) {
        String subBreed = subBreedsList.get(position);
        holder.subBreed.setText(subBreed.substring(0,1).toUpperCase() + subBreed.substring(1));
    }

    @Override
    public int getItemCount() {
        return subBreedsList.size();
    }

    class SubBreedViewHolder extends RecyclerView.ViewHolder{

        TextView subBreed;

        public SubBreedViewHolder(@NonNull View itemView) {
            super(itemView);
            subBreed = itemView.findViewById(R.id.dog_sub_breed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String subBreed = subBreedsList.get(getLayoutPosition());
                    onSubBreedClickListener.onSubBreedClick(subBreed);
                }
            });
        }
    }
    public interface OnSubBreedClickListener{
        void onSubBreedClick(String breed);
    }
}
