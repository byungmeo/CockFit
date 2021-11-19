package com.kbd.cockfit;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {
    private TextView textView_name;
    private TextView textView_proof;
    private TextView textView_base;
    private TextView textView_ingredient;
    private TextView textView_equipment;
    private TextView textView_description;
    private TextView textView_tags;
    private ImageView image;
    private Toolbar appBarRecipe;
    private Recipe recipe;
    private boolean isFavorite = false;
    private MenuItem menuItem_bookmark;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseAuth mAuth;
    private ArrayList<Recipe> favoriteRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setSupportActionBar(findViewById(R.id.topAppBarRecipe));
        image = findViewById(R.id.recipe_imageView);
        textView_name = findViewById(R.id.recipe_textview_name);
        textView_proof = findViewById(R.id.recipe_textview_proof);
        textView_base = findViewById(R.id.recipe_textview_base);
        textView_ingredient = findViewById(R.id.recipe_textview_ingredient);
        textView_equipment = findViewById(R.id.recipe_textview_equipment);
        textView_description = findViewById(R.id.recipe_textview_description);
        textView_tags = findViewById(R.id.recipe_textView_tags);
        appBarRecipe = findViewById(R.id.topAppBarRecipe);
        setSupportActionBar(appBarRecipe);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        uid = mAuth.getUid();

        appBarRecipe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeActivity.this.onBackPressed();
            }
        });


        int recipeNumber = getIntent().getIntExtra("recipe_number", 0);

        if(recipeNumber == 0) {
            Log.d("test", "나만의 레시피임");
            recipe = getIntent().getParcelableExtra("recipe");
        } else {
            recipe = getRecipe(recipeNumber);

            favoriteRecipeList = new ArrayList<>();

            mDatabase.child("user").child(uid).child("favorite").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteRecipeList.clear();
                    for (DataSnapshot numberSnapshot : snapshot.getChildren()) {
                        int firebaseRecipeNumber = numberSnapshot.getValue(int.class);
                        favoriteRecipeList.add(getRecipe(firebaseRecipeNumber));
                    }
                    for(Recipe favor : favoriteRecipeList) {
                        if(favor.getNumber() == recipe.getNumber()) {
                            isFavorite = true;
                            break;
                        }
                    }
                    Log.d("테스트1", String.valueOf(favoriteRecipeList.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        textView_name.setText(recipe.getName());
        textView_proof.setText(recipe.getProof()+"%");
        textView_base.setText(recipe.getBase());
        image.setImageBitmap(recipe.getImage());

        List<String> ingredientList = recipe.getIngredient();
        String text_ingredient = "";
        for (String ingredient : ingredientList) {
            text_ingredient += ingredient + " ";
        }
        textView_ingredient.setText(text_ingredient);


        String text_equipment = "";
        for (String equipment : recipe.getEquipment()) {
            text_equipment += equipment + " ";
        }
        textView_equipment.setText(text_equipment);

        textView_description.setText(recipe.getDescription());

        String text_tags = "";
        for (String tag : recipe.getTags()) {
            text_tags += tag + " ";
        }
        textView_tags.setText(text_tags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_recipe, menu);
        appBarRecipe.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Map<String, Object> childUpdates = new HashMap<>();
                if(!isFavorite) {
                    favoriteRecipeList.add(recipe);
                    Log.d("테스트2", String.valueOf(favoriteRecipeList.size()));

                    Toast.makeText(RecipeActivity.this, "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    childUpdates.put(String.valueOf(favoriteRecipeList.size() - 1), recipe.getNumber());
                    mDatabase.child("user").child(uid).child("favorite").updateChildren(childUpdates); //즐겨찾기 목록(Firebase)에 추가

                    isFavorite = true;
                } else {
                    for(Recipe favorite : favoriteRecipeList) {
                        if(favorite.getNumber() == recipe.getNumber()) {
                            favoriteRecipeList.remove(favorite);
                            break;
                        }
                    }
                    int i = 0;
                    for(Recipe favoriteRecipe : favoriteRecipeList) {
                        childUpdates.put(String.valueOf(i++), favoriteRecipe.getNumber());
                    }
                    DatabaseReference databaseReference = mDatabase.child("user").child(uid).child("favorite");
                    databaseReference.setValue(childUpdates);

                    Toast.makeText(RecipeActivity.this, "즐겨찾기 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    isFavorite = false;
                }

                return true;
            }
        });

        // change color for icon 0
        mDatabase.child("user").child(uid).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isBookmarked = false;
                for(DataSnapshot numberSnapshot : snapshot.getChildren()) {
                    int recipeNumber = numberSnapshot.getValue(int.class);
                    if(recipeNumber == recipe.getNumber()) {
                        Log.d("test", "onDataChange: ");
                        menuItem_bookmark.setIcon(R.drawable.ic_baseline_bookmark_true_24);
                        isBookmarked = true;
                        break;
                    }
                }
                if(!isBookmarked){
                    menuItem_bookmark.setIcon(R.drawable.ic_baseline_bookmark_false_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        menuItem_bookmark = menu.getItem(0);
        Drawable drawable = menuItem_bookmark.getIcon();
        drawable.mutate();
        return true;
    }

    public Recipe getRecipe(int recipeNumber) {
        Recipe recipe = null;

        try {
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                int number = jo.getInt("number");

                if(number != recipeNumber) {
                    continue;
                }

                String name = jo.getString("name");
                String proof = jo.getString("proof");
                String base = jo.getString("base");
                String[] ingredient = UtilitySet.jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = UtilitySet.jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = UtilitySet.jsonArrayToArray(jo.getJSONArray("tags"));

                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/recipe/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                recipe = new Recipe(number, bitmap, name, proof, base, ingredient, equipment, description, tags);
                break;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }


}
