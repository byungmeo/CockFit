package com.kbd.cockfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class PanelFragment extends Fragment {
    private RecyclerView recyclerView;
    private HotRecipeAdapter hotRecipeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_panel, container, false);

        recyclerView = v.findViewById(R.id.panel_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        hotRecipeAdapter = new HotRecipeAdapter(MainActivity.hotRecipeArrayList);
        recyclerView.setAdapter(hotRecipeAdapter);

        return v;
    }

    private class HotRecipeAdapter extends RecyclerView.Adapter<HotRecipeAdapter.HotRecipeViewHolder> {
        private ArrayList<HotRecipe> hotRecipeArrayList;

        public HotRecipeAdapter(ArrayList<HotRecipe> hotRecipeArrayList) {
            this.hotRecipeArrayList = hotRecipeArrayList;
        }

        @NonNull
        @Override
        public HotRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_recipe_item_layout, parent, false);
            HotRecipeViewHolder holder = new HotRecipeViewHolder(view);
            holder.imageView_image.setOnClickListener(new UtilitySet.OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    HotRecipe recipe = MainActivity.hotRecipeArrayList.get(holder.getAdapterPosition());
                    Intent intent = new Intent(getContext(), PostActivity.class);
                    intent.putExtra("postId", recipe.hotRecipeInfo.postId);
                    intent.putExtra("forumT", "share");
                    intent.putExtra("post", recipe.post);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HotRecipeViewHolder holder, int position) {
            HotRecipe hotRecipe = hotRecipeArrayList.get(position);

            Glide.with(getContext())
                    .load(hotRecipe.imageUri)
                    .into(holder.imageView_image);

            holder.textView_name.setText(hotRecipe.name);
        }

        @Override
        public int getItemCount() {
            return this.hotRecipeArrayList.size();
        }

        public class HotRecipeViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView_image;
            private TextView textView_name;

            public HotRecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                this.imageView_image = itemView.findViewById(R.id.hot_imageView_image);
                this.textView_name = itemView.findViewById(R.id.hot_textView_name);
            }
        }
    }
}