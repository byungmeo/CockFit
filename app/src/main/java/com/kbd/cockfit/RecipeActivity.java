package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeActivity extends AppCompatActivity {

    TextView textView_name;
    TextView textView_proof;
    TextView textView_base;
    TextView textView_ingredient;
    TextView textView_equipment;
    TextView textView_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        textView_name = findViewById(R.id.recipe_textview_name);
        textView_proof = findViewById(R.id.recipe_textview_proof);
        textView_base = findViewById(R.id.recipe_textview_base);
        textView_ingredient = findViewById(R.id.recipe_textview_ingredient);
        textView_equipment = findViewById(R.id.recipe_textview_equipment);
        textView_description = findViewById(R.id.recipe_textview_description);

        int recipeNumber = getIntent().getIntExtra("recipe_number", 0);
        if(recipeNumber == 0) {
            Log.d("error", "extra를 못받음");
            return;
        }

        Recipe recipe = getRecipe(recipeNumber);

        textView_name.setText(recipe.getName());
        textView_proof.setText(recipe.getProof()+"%");
        textView_base.setText(recipe.getBase());
        textView_ingredient.setText(recipe.getIngredient());
        textView_equipment.setText(recipe.getEquipment());
        textView_description.setText(recipe.getDescription());


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

                recipe = new Recipe(number, name, proof, base, ingredient[0], equipment[0], description, tags);
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
