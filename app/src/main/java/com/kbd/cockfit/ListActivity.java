package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recipeRecycler;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initRecipeRecycler();
    }

    public void initRecipeRecycler() {
        recipeRecycler = findViewById(R.id.list_recycler);
        recipeArrayList = new ArrayList<>();
        initRecipeList();
        recipeAdapter = new RecipeAdapter(this, recipeArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recipeRecycler.setLayoutManager(linearLayoutManager);
        recipeRecycler.setAdapter(recipeAdapter);
    }

    public void initRecipeList() {
        recipeArrayList.add(new Recipe("병머의 의지", "진", new String[]{"#맛있음", "#예쁨"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
        recipeArrayList.add(new Recipe("이건 테스트", "럼", new String[]{"#테스트", "#성공"}));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickBackButton(View view) {
        onBackPressed();
    }

    public void clickTest(View view) {
        Intent in = new Intent(this, RecipeActivity.class);
        startActivity(in);
    }
}