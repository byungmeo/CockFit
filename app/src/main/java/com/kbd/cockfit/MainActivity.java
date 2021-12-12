package com.kbd.cockfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private PanelFragment panelFragment = new PanelFragment();
    private MyRecipeFragment myRecipeFragment = new MyRecipeFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private CocktailInfoFragment cocktailInfoFragment = new CocktailInfoFragment();
    public static ArrayList<Recipe> recipeArrayList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Thread를 방해하지 않도록 별도의 Thread에서 데이터를 로드랍니다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData(); //몇몇 데이터는 미리 로그인 시 불러옵니다.
            }
        }).start();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, panelFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

        toolbar = findViewById(R.id.topAppBarFragment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.main_menuItem_profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    public void loadData() {
        try {
            if(recipeArrayList != null) {
                //이미 데이터를 불러왔으므로, 다시 불러오진 않습니다.
                return;
            }

            recipeArrayList = new ArrayList<>();
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int number = jo.getInt("number");
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

                recipeArrayList.add(new Recipe(number, bitmap, name, proof, base, ingredient, equipment, description, tags));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.page_panel:
                    transaction.replace(R.id.frameLayout, panelFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_myRecipe:
                    transaction.replace(R.id.frameLayout, myRecipeFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_community:
                    transaction.replace(R.id.frameLayout, communityFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_cocktailInfo:
                    transaction.replace(R.id.frameLayout, cocktailInfoFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //메인화면에서는 뒤로가기 버튼을 누르면 종료여부를 물어봅니다
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finishAndRemoveTask();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}