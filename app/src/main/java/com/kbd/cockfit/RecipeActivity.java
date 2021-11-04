package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Recipe r1 = new Recipe("진영의 낭만", 17, "보드카", "석류주스, 블루 큐라소", "쉐이커, 스트레이너", "1. 보드카 30ml를 쉐이커에 넣는다 \n2. 석류주스 60ml, 블루 큐라소 30ml를 쉐이커에 넣는다");
        Recipe r2 = new Recipe("문희의 감성", 85, "럼", "앙고스투라 비터, 오렌지주스, 그레나딘 시럽", "믹싱글라스, 스트레이너, 바스푼", "1. 얼음, 럼 45ml, 오렌지주스 60ml, 그레나딘 시럽 30ml를 믹싱글라스에 넣는다\n2. 바스푼으로 잘 섞어준다\n3. 스트레이너로 얼음을 거르고 콜린스 잔에 잘 따라준다");

        TextView recipe_textview_n = (TextView) findViewById(R.id.recipe_textview_name);
        TextView recipe_textview_p = (TextView) findViewById(R.id.recipe_textview_proof);
        TextView recipe_textview_b = (TextView) findViewById(R.id.recipe_textview_base);
        TextView recipe_textview_i = (TextView) findViewById(R.id.recipe_textview_ingredient);
        TextView recipe_textview_e = (TextView) findViewById(R.id.recipe_textview_equipment);
        TextView recipe_textview_d = (TextView) findViewById(R.id.recipe_textview_description);

        Recipe r = getRecipe(2); //실제로는 putExtra를 통해 받아온 레시피번호를 기준으로 레시피를 받아옵니다.

        recipe_textview_n.setText(r.name);
        recipe_textview_p.setText(r.proof+"%");
        recipe_textview_b.setText(r.base);
        recipe_textview_i.setText(r.ingredient);
        recipe_textview_e.setText(r.equipment);
        recipe_textview_d.setText(r.description);

    }

    public Recipe getRecipe(int recipeNumber) {
        Recipe recipe = null;

        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream is = assetManager.open("jsons/basicRecipe.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            JSONArray jsonArray = new JSONArray(jsonData);
            String s = "";

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                int number = jo.getInt("number");

                if(number != recipeNumber) {
                    continue;
                }

                String name = jo.getString("name");
                int proof = jo.getInt("proof");
                String base = jo.getString("base");
                String[] ingredient = toStringArray(jo.getJSONArray("ingredient"));
                String[] equipment = toStringArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");

                recipe = new Recipe(name, proof, base, ingredient[0], equipment[0], description);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    //다른 곳에서 쓸 수도 있으므로, static으로 우선 선언
    public static String[] toStringArray(JSONArray array) {
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

    public class Recipe {

        public String name=""; //칵테일 이름
        public int proof=0; //칵테일 도수
        public String base=""; //칵테일 기주
        public String ingredient=""; //칵테일 재료
        public String equipment=""; //칵테일 장비
        public String description=""; //칵테일 제조에 대한 상세설명


        public Recipe(String name, int proof, String base, String ingredient, String equipment, String description) {
            this.name = name;
            this.proof = proof;
            this.base = base;
            this.ingredient = ingredient;
            this.equipment = equipment;
            this.description = description;
        }

    }
}
