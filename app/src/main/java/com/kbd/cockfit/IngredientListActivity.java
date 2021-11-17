package com.kbd.cockfit;

import android.content.Context;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IngredientListActivity extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        initIngredientRecycler();

        RecyclerView ingredientRecyclerView = (RecyclerView)findViewById(R.id.ingredient_list_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        ingredientRecyclerView.setLayoutManager(manager); // LayoutManager 등록
        ingredientRecyclerView.setAdapter(new IngredientAdapter(ingredientArrayList));  // Adapter 등록
    }

    public void initIngredientRecycler() {

        ingredientArrayList = new ArrayList<>();

        ingredientArrayList.add(new Ingredient(R.drawable.simple_syrup, "심플 시럽", "설탕과 물을 끓여서 만든 시럽\n\n플레인 시럽, 설탕 시럽이라고도 부름\n\n주로 단 맛을 내는 칵테일에 사용"));
        ingredientArrayList.add(new Ingredient(R.drawable.grenadine_syrup, "그레나딘 시럽", "설탕과 석류를 섞어서 만든 시럽\n\n붉은 색을 띄고 달콤한 맛을 냄"));
        ingredientArrayList.add(new Ingredient(R.drawable.lemon_juice, "레몬 주스", "레몬즙을 짜서 모아놓은 제품\n\n생레몬즙을 사용하기 어려울 때 대체하여 사용"));
        ingredientArrayList.add(new Ingredient(R.drawable.lime_juice, "라임 주스", "라임즙을 짜서 모아놓은 제품\n\n생라임즙을 사용하기 어려울 때 대체하여 사용"));


    }

    public void clickButton(View view) {
        if(view.getId() == R.id.list_button_backButton) {
            this.onBackPressed();
        }
    }
    /*private String getJsonString() {
        String json = "";

        try {
            InputStream is = getAssets().open("basicIngre.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }

    public class Ingredient{

        private int number;
        private String name;
        private String description;

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setNumber(int number) {
            this.number=number;
        }

        public void setName(String name) {
            this.name=name;
        }

        public void setDescription(String description) {
            this.description=description;
        }
    }

    private void jsonParsing(String json) {
        try{
            List ingredientList = new ArrayList();
            JSONObject jsonObject = new JSONObject(json);

            JSONArray ingredientArray = jsonObject.getJSONArray("ingre");

            for(int i=0; i<ingredientArray.length(); i++) {
                JSONObject ingredientObject = ingredientArray.getJSONObject(i);

                Ingredient ingredient = new Ingredient();

                ingredient.setNumber(ingredientObject.getInt("number"));
                ingredient.setName(ingredientObject.getString("name"));
                ingredient.setDescription(ingredientObject.getString("description"));

                ingredientList.add(ingredient);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public static class Ingredient {

        private int imageResourceID;
        private String name;
        private String description;

        public Ingredient(int id, String name, String description) {
            this.imageResourceID = id;
            this.name=name;
            this.description=description;
        }

        public int getImageResourceID() {
            return imageResourceID;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setImageResourceID(int imageResourceID) {
            this.imageResourceID = imageResourceID;
        }

        public void setName(String name) {
            this.name=name;
        }

        public void setDescription(String description) {
            this.description=description;
        }

    }

    //Adapter
    public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Ingredient> ingredientArrayList = null;

        IngredientAdapter(ArrayList<Ingredient> ingredientList) {
            this.ingredientArrayList=ingredientList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.ingredient_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new IngredientViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) viewHolder;

            ingredientViewHolder.imageView.setImageResource(ingredientArrayList.get(position).getImageResourceID());
            ingredientViewHolder.name.setText(ingredientArrayList.get(position).getName());
            ingredientViewHolder.description.setText(ingredientArrayList.get(position).getDescription());


        }

        @Override
        public int getItemCount() {
            return ingredientArrayList.size();
        }

        //ViewHolder 구현
        public class IngredientViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;
            TextView description;

            public IngredientViewHolder(View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.ingredient_image);
                name=itemView.findViewById(R.id.ingredient_name);
                description=itemView.findViewById(R.id.ingredient_description);

            }

        }
    }
}