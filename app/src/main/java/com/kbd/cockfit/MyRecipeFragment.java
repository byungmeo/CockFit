package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {
    private RecyclerView myRecipeRecyclerView;
    private GridLayoutManager layoutManager;
    private MyRecipeAdapter adapter;
    private ArrayList<MyRecipe> myRecipeArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_recipe, container, false);

        myRecipeArrayList = new ArrayList<>();
        initMyRecipeList();

        layoutManager = new GridLayoutManager(v.getContext(), 2);

        myRecipeRecyclerView = (RecyclerView) v.findViewById(R.id.myRecipe_recyclerView);
        myRecipeRecyclerView.setHasFixedSize(true);

        myRecipeRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new MyRecipeAdapter(myRecipeArrayList);
        myRecipeRecyclerView.setAdapter(adapter);

        return v;
    }

    public void initMyRecipeList() {
        myRecipeArrayList.add(new MyRecipe("나만의레시피1"));
        myRecipeArrayList.add(new MyRecipe("나만의레시피2"));
        myRecipeArrayList.add(new MyRecipe("나만의레시피3"));
        myRecipeArrayList.add(new MyRecipe("나만의레시피4"));
        myRecipeArrayList.add(new MyRecipe("나만의레시피5"));
    }

    private class MyRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_ITEM = 2;

        private ArrayList<MyRecipe> myRecipeArrayList;


        public MyRecipeAdapter(ArrayList<MyRecipe> myRecipeArrayList) {
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
                return new ItemViewHolder(view);
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
                itemViewHolder.name.setText(myRecipeArrayList.get(position).getName());

                itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeActivity.class);
                        context.startActivity(intent);
                    }
                });
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

    private class MyRecipe {
        private String name;

        public MyRecipe(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}