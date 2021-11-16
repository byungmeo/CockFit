package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recipeRecycler;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeArrayList;
    private ArrayList<Recipe> sortRecipeList;
    private TextView textView_screenName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        progressBar = findViewById(R.id.list_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        textView_screenName = findViewById(R.id.list_textView_screenName);

        initRecipeRecycler();
    }

    public void initRecipeRecycler() {
        recipeRecycler = findViewById(R.id.list_recycler);
        recipeArrayList = new ArrayList<>();
        sortRecipeList = new ArrayList<>();
        String keyword = getIntent().getStringExtra("keyword");

        if(keyword.equals("every")) {
            textView_screenName.setText("모든 레시피 목록"); //우선 즐겨찾기 레시피 목록은 제외
        } else if(keyword.equals("favorite")) {
            textView_screenName.setText("즐겨찾기한 레시피 목록");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recipeRecycler.setLayoutManager(linearLayoutManager);

        loadCommonRecipeList();

        if(keyword.equals("every")) {
            sortRecipeList = recipeArrayList;
            recipeAdapter = new RecipeAdapter(this, sortRecipeList);
            recipeRecycler.setAdapter(recipeAdapter);
            progressBar.setVisibility(View.GONE);
        } else if(keyword.equals("favorite")) {
            loadFavoriteRecipeList();
        }
    }

    public void loadCommonRecipeList() {
        try {
            String jsonData = RecipeActivity.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int number = jo.getInt("number");
                String name = jo.getString("name");
                String proof = jo.getString("proof");
                String base = jo.getString("base");
                String[] ingredient = RecipeActivity.jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = RecipeActivity.jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = RecipeActivity.jsonArrayToArray(jo.getJSONArray("tags"));

                recipeArrayList.add(new Recipe(number, name, proof, base, ingredient, equipment, description, tags));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadFavoriteRecipeList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        String uid = mAuth.getUid();
        ArrayList<Recipe> favoriteRecipeList = new ArrayList<>();

        mDatabase.child("user").child(uid).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteRecipeList.clear();
                for(DataSnapshot numberSnapshot : snapshot.getChildren()) {
                    int recipeNumber = numberSnapshot.getValue(int.class);
                    for(Recipe recipe : recipeArrayList) {
                        if(recipe.getNumber() == recipeNumber) {
                            favoriteRecipeList.add(recipe);
                            break;
                        }
                    }
                }
                sortRecipeList = favoriteRecipeList;
                recipeAdapter = new RecipeAdapter(ListActivity.this, sortRecipeList);
                recipeRecycler.setAdapter(recipeAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clickButton(View view) {
        String keyword = getIntent().getStringExtra("keyword");

        if(view.getId() == R.id.list_button_backButton) {
            this.onBackPressed();
        } else if(view.getId() == R.id.list_button_sort) {
            final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            getMenuInflater().inflate(R.menu.list_sort_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.sortMenu_proof_desc) {
                        //높은 도수순
                        Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe o1, Recipe o2) {
                                int proof1 = Integer.parseInt(o1.getProof());
                                int proof2 = Integer.parseInt(o2.getProof());
                                if (proof1 < proof2) {
                                    return 1;
                                } else if (proof1 == proof2) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }
                        });
                    } else if (item.getItemId() == R.id.sortMenu_proof_asc) {
                        //낮은 도수순
                        Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe o1, Recipe o2) {
                                int proof1 = Integer.parseInt(o1.getProof());
                                int proof2 = Integer.parseInt(o2.getProof());
                                if (proof1 > proof2) {
                                    return 1;
                                } else if (proof1 == proof2) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }
                        });
                    } else if (item.getItemId() == R.id.sortMenu_name_asc) {
                        //이름순 a-z
                        Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe o1, Recipe o2) {
                                String name1 = o1.getName();
                                String name2 = o2.getName();
                                if (name1.compareTo(name2) > 0) {
                                    return 1;
                                } else if (name1.compareTo(name2) == 0) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }
                        });
                    } else if (item.getItemId() == R.id.sortMenu_name_desc) {
                        //이름순 a-z
                        Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe o1, Recipe o2) {
                                String name1 = o1.getName();
                                String name2 = o2.getName();
                                if (name1.compareTo(name2) < 0) {
                                    return 1;
                                } else if (name1.compareTo(name2) == 0) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }
                        });
                    }
                    recipeAdapter.notifyDataSetChanged();
                    return false;
                }
            });
            popupMenu.show();
        }
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

            recipeViewHolder.name.setText(recipeArrayList.get(position).getName());
            recipeViewHolder.base.setText("베이스 : ");
            recipeViewHolder.tags.setText("태그 : ");
            recipeViewHolder.proof.setText("도수 : ");

            String baseText = recipeViewHolder.base.getText().toString();
            String tagsText = recipeViewHolder.tags.getText().toString();
            baseText += (recipeArrayList.get(position).getBase());
            for(String tag : recipeArrayList.get(position).getTags()) {
                tagsText += tag + " ";
            }
            String proofText = recipeViewHolder.proof.getText().toString();
            proofText += (recipeArrayList.get(position).getProof());

            recipeViewHolder.base.setText(baseText);
            recipeViewHolder.tags.setText(tagsText);
            recipeViewHolder.proof.setText(proofText);

            recipeViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeActivity.class);
                    intent.putExtra("recipe_number", recipeArrayList.get(holder.getAdapterPosition()).getNumber());
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
            private TextView proof;

            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                card = itemView.findViewById(R.id.recipeitem_cardView);
                name = itemView.findViewById(R.id.recipeitem_name);
                base = itemView.findViewById(R.id.recipeitem_base);
                tags = itemView.findViewById(R.id.recipeitem_tags);
                proof = itemView.findViewById(R.id.recipeitem_proof);
            }
        }
    }
}