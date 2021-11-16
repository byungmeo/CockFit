package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView myRecipeRecyclerView;
    private GridLayoutManager layoutManager;
    private MyRecipeAdapter adapter;
    private ArrayList<Recipe> myRecipeArrayList;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_recipe, container, false);

        progressBar = v.findViewById(R.id.myRecipe_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        layoutManager = new GridLayoutManager(v.getContext(), 2);
        myRecipeRecyclerView = v.findViewById(R.id.myRecipe_recyclerView);
        myRecipeRecyclerView.setHasFixedSize(true);
        myRecipeRecyclerView.setLayoutManager(layoutManager);
        myRecipeRecyclerView.setVisibility(View.INVISIBLE);

        myRecipeArrayList = new ArrayList<>();
        adapter = new MyRecipeAdapter(myRecipeArrayList);
        myRecipeRecyclerView.setAdapter(adapter);

        initMyRecipeList();

        return v;
    }

    public void initMyRecipeList() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        mDatabase.child("user").child(uid).child("MyRecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRecipeArrayList.clear();
                
                for (DataSnapshot recipeSnapshot: snapshot.getChildren()) {
                    myRecipeArrayList.add(recipeSnapshot.getValue(Recipe.class));
                }

                adapter = new MyRecipeAdapter(myRecipeArrayList);
                myRecipeRecyclerView.setAdapter(adapter);
                myRecipeRecyclerView.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyRecipeLoad", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private class MyRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_ITEM = 2;

        private ArrayList<Recipe> myRecipeArrayList;


        public MyRecipeAdapter(ArrayList<Recipe> myRecipeArrayList) {
            this.myRecipeArrayList = myRecipeArrayList;
        }


        public void onItemClick(int position) {
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            private CardView cardView;
            private TextView name;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.myReicipeItem_cardView);
                name = itemView.findViewById(R.id.myRecipeItem_textView_name);
            }
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            private CardView cardView;

            public FooterViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.addMyRecipe_cardView);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrecipeitem_layout, parent, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeActivity.class);
                        intent.putExtra("recipe", myRecipeArrayList.get(itemViewHolder.getAdapterPosition()));
                        context.startActivity(intent);
                    }
                });
                return itemViewHolder;
            } else if(viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmyrecipe_layout, parent, false);
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(myRecipeArrayList.get(holder.getAdapterPosition()).getName());

            } else if(holder instanceof FooterViewHolder) {
                FooterViewHolder footViewHolder = (FooterViewHolder) holder;
                footViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MakeRecipeActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return myRecipeArrayList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == myRecipeArrayList.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
    }
}