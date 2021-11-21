package com.kbd.cockfit;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    private ImageView imageView_picture;
    private Toolbar appBarRecipe;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Recipe recipe;
    private boolean isFavorite = false;
    private MenuItem menuItem_bookmark;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseAuth mAuth;
    private ArrayList<Recipe> favoriteRecipeList;
    private boolean isMyRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setSupportActionBar(findViewById(R.id.topAppBarRecipe));
        imageView_picture = findViewById(R.id.recipe_imageView);
        textView_name = findViewById(R.id.recipe_textview_name);
        textView_proof = findViewById(R.id.recipe_textview_proof);
        textView_base = findViewById(R.id.recipe_textview_base);
        textView_ingredient = findViewById(R.id.recipe_textview_ingredient);
        textView_equipment = findViewById(R.id.recipe_textview_equipment);
        textView_description = findViewById(R.id.recipe_textview_description);
        textView_tags = findViewById(R.id.recipe_textView_tags);
        appBarRecipe = findViewById(R.id.topAppBarRecipe);
        setSupportActionBar(appBarRecipe);
        progressBar = findViewById(R.id.recipe_progressBar);
        scrollView = findViewById(R.id.scrollView2);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
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
            isMyRecipe = true;
            MyRecipe myRecipe = getIntent().getParcelableExtra("recipe");
            recipe = myRecipe;
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            mStorage.child("Users").child(uid).child("CocktailImage").child(myRecipe.getMyRecipeId() + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(RecipeActivity.this)
                            .load(task.getResult())
                            .into(imageView_picture);
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            isMyRecipe = false;
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            imageView_picture.setImageBitmap(recipe.getImage());
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

        textView_name.setText(recipe.getName());
        textView_proof.setText(recipe.getProof()+"%");
        textView_base.setText(recipe.getBase());

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
        if(isMyRecipe) {
            menu.setGroupVisible(0, false);
        }
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
