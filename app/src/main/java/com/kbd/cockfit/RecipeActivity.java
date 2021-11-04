package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        Recipe r = getRecipe(2); //실제로는 putExtra를 통해 받아온 레시피번호를 기준으로 레시피를 받아옵니다.

        textView_name.setText(r.name);
        textView_proof.setText(r.proof+"%");
        textView_base.setText(r.base);
        textView_ingredient.setText(r.ingredient);
        textView_equipment.setText(r.equipment);
        textView_description.setText(r.description);

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
                int proof = jo.getInt("proof");
                String base = jo.getString("base");
                String[] ingredient = jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = RecipeActivity.jsonArrayToArray(jo.getJSONArray("tags"));

                recipe = new Recipe(name, proof, base, ingredient[0], equipment[0], description, tags);
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
