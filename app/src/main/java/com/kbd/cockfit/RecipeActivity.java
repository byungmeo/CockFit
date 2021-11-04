package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        Recipe r = r1;

        recipe_textview_n.setText(r.name);
        recipe_textview_p.setText(r.proof+"%");
        recipe_textview_b.setText(r.base);
        recipe_textview_i.setText(r.ingredient);
        recipe_textview_e.setText(r.equipment);
        recipe_textview_d.setText(r.description);

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
