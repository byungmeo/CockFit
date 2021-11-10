package com.kbd.cockfit;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    private ImageButton imageButton_bookmark;
    private TextView textView_name;
    private TextView textView_proof;
    private TextView textView_base;
    private TextView textView_ingredient;
    private TextView textView_equipment;
    private TextView textView_description;
    private TextView textView_tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        imageButton_bookmark = findViewById(R.id.recipe_button_bookmark);
        textView_name = findViewById(R.id.recipe_textview_name);
        textView_proof = findViewById(R.id.recipe_textview_proof);
        textView_base = findViewById(R.id.recipe_textview_base);
        textView_ingredient = findViewById(R.id.recipe_textview_ingredient);
        textView_equipment = findViewById(R.id.recipe_textview_equipment);
        textView_description = findViewById(R.id.recipe_textview_description);
        textView_tags = findViewById(R.id.recipe_textView_tags);

        Recipe recipe = null;

        int recipeNumber = getIntent().getIntExtra("recipe_number", 0);

        if(recipeNumber == 0) {
            Log.d("test", "나만의 레시피임");
            recipe = getIntent().getParcelableExtra("recipe");
        } else {
            recipe = getRecipe(recipeNumber);

            imageButton_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    String uid = mAuth.getUid();

                    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                    ArrayList<Recipe> favoriteRecipeList = new ArrayList<>();

                    for(Recipe recipe : favoriteRecipeList) { //즐겨찾기 목록에서 삭제
                        if(recipe.getNumber() == recipeNumber) {
                            DatabaseReference databaseReference = mDatabase.child("user").child(uid).child("favorite").child("즐겨찾기 " + String.valueOf(recipeNumber) + "번");
                            databaseReference.removeValue();
                            favoriteRecipeList.remove(recipe);

                            Toast.makeText(view.getContext(), "즐겨찾기 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    mDatabase.child("user").child(uid).child("favorite").child("즐겨찾기 " + String.valueOf(recipeNumber) + "번").setValue(recipeNumber); //즐겨찾기 목록에 추가

                    try {
                        String jsonData = RecipeActivity.jsonToString(RecipeActivity.this, "jsons/basicRecipe.json");
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
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

                    mDatabase.child("user").child(uid).child("favorite").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot numberSnapshot : snapshot.getChildren()) {
                                int recipeNumber = numberSnapshot.getValue(int.class);
                                for (Recipe recipe : recipeArrayList) {
                                    if (recipe.getNumber() == recipeNumber) {
                                        favoriteRecipeList.add(recipe);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(view.getContext(), "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                }
            });
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

    public Recipe getRecipe(int recipeNumber) {
        Recipe recipe = null;

        try {
            String jsonData = jsonToString(this, "jsons/basicRecipe.json");
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
                String[] ingredient = jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = RecipeActivity.jsonArrayToArray(jo.getJSONArray("tags"));

                recipe = new Recipe(number, name, proof, base, ingredient, equipment, description, tags);
                break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    public static String jsonToString(Context context, String filePath) {
        String jsonData = null;

        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream is = assetManager.open("jsons/basicRecipe.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            jsonData = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    //다른 곳에서 쓸 수도 있으므로, static으로 우선 선언
    public static String[] jsonArrayToArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

    public void onBackButtonTapped(View view) {
        super.onBackPressed();
    }
}
