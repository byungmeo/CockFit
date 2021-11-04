package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recipeRecycler;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeArrayList;
    private TextView screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        screenName = findViewById(R.id.list_textView_screenName);
        initRecipeRecycler();
    }

    public void initRecipeRecycler() {
        recipeRecycler = findViewById(R.id.list_recycler);
        recipeArrayList = new ArrayList<>();
        String keyword = getIntent().getStringExtra("keyword");

        switch (keyword) {
            case "every" : {
                screenName.setText("모든 레시피 목록");
                initRecipeList();
                break;
            }
            case "favorite" : {
                screenName.setText("즐겨찾기한 레시피 목록");
                initFavoriteList();
                break;
            }
            default : {
                break;
            }
        }

        recipeAdapter = new RecipeAdapter(this, recipeArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recipeRecycler.setLayoutManager(linearLayoutManager);
        recipeRecycler.setAdapter(recipeAdapter);
    }

    public void initRecipeList() {
        try {
            String jsonData = RecipeActivity.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                int number = jo.getInt("number");
                String name = jo.getString("name");
                int proof = jo.getInt("proof");
                String base = jo.getString("base");
                String[] ingredient = RecipeActivity.jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = RecipeActivity.jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = RecipeActivity.jsonArrayToArray(jo.getJSONArray("tags"));

                recipeArrayList.add(new Recipe(number, name, proof, base, ingredient[0], equipment[0], description, tags));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initFavoriteList() {
        try {
            String jsonData = RecipeActivity.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                int number = jo.getInt("number");
                String name = jo.getString("name");
                int proof = jo.getInt("proof");
                String base = jo.getString("base");
                String[] ingredient = RecipeActivity.jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = RecipeActivity.jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = RecipeActivity.jsonArrayToArray(jo.getJSONArray("tags"));

                recipeArrayList.add(new Recipe(number, name, proof, base, ingredient[0], equipment[0], description, tags));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickBackButton(View view) {
        onBackPressed();
    }

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

            recipeViewHolder.name.setText(recipeArrayList.get(position).name);
            String baseText = recipeViewHolder.base.getText().toString();
            String tagsText = recipeViewHolder.tags.getText().toString();
            baseText += (recipeArrayList.get(position).base);
            for(String tag : recipeArrayList.get(position).tags) {
                tagsText += tag + " ";
            }
            recipeViewHolder.base.setText(baseText);
            recipeViewHolder.tags.setText(tagsText);

            recipeViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeActivity.class);
                    intent.putExtra("recipe_number", recipeArrayList.get(holder.getAdapterPosition()).number);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipeArrayList.size();
        }

        public class RecipeViewHolder extends RecyclerView.ViewHolder {
            private int number;
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
}