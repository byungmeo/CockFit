package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipeArrayList;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipeitem_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

        recipeViewHolder.base.setText("베이스 : ");
        recipeViewHolder.tags.setText("태그 : ");

        recipeViewHolder.name.setText(recipeArrayList.get(position).getName().toString());
        String baseText = recipeViewHolder.base.getText().toString();
        String tagsText = recipeViewHolder.tags.getText().toString();
        baseText += (recipeArrayList.get(position).getBase().toString());
        for(String tag : recipeArrayList.get(position).getTags()) {
            tagsText += tag + " ";
        }
        recipeViewHolder.base.setText(baseText);
        recipeViewHolder.tags.setText(tagsText);

        recipeViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private TextView name;
        private TextView base;
        private TextView tags;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.recipeitem_cardView);
            name = itemView.findViewById(R.id.recipeitem_name);
            base = itemView.findViewById(R.id.recipeitem_base);
            tags = itemView.findViewById(R.id.recipeitem_tags);
        }
    }
}
